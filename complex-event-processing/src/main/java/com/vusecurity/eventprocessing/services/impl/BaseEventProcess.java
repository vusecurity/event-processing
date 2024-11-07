/*
 ***************************************************************************************
 *  Copyright (C) 2024 VU, Inc. All rights reserved.                                   *
 *  https://www.vusecurity.com/                                                        *
 *  ---------------------------------------------------------------------------------- *
 *  This program is free software; you can redistribute it and/or modify               *
 *  it under the terms of the GNU General Public License version 2                     *
 *  as published by the Free Software Foundation.                                      *
 *                                                                                     *
 *  This program is distributed in the hope that it will be useful,                    *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of                     *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      *
 *  GNU General Public License for more details.                                       *
 *                                                                                     *
 *  You should have received a copy of the GNU General Public License along            *
 *  with this program; if not, write to the Free Software Foundation, Inc.,            *
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.                        *
 ***************************************************************************************
 */

package com.vusecurity.eventprocessing.services.impl;

import com.espertech.esper.common.client.EPCompiled;
import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.common.client.util.PropertyResolutionStyle;
import com.espertech.esper.common.client.util.StatementProperty;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.espertech.esper.runtime.client.*;
import com.vusecurity.eventprocessing.models.Event;
import com.vusecurity.eventprocessing.models.Transaction;
import com.vusecurity.eventprocessing.services.IEventProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PreDestroy;

import java.util.List;

public abstract class BaseEventProcess implements IEventProcessService {

    private final Logger logger = LoggerFactory.getLogger(BaseEventProcess.class);

    EPRuntime runtime;

    @Override
    public void sendEvent(Event event) {
        String eventType = event instanceof Transaction ? "Transaction" : "Event";
        runtime.getEventService().sendEventBean(event, eventType);
    }

    protected abstract Configuration getConfiguration();

    @Override
    public void initialize(List<EventProcessListener> listeners) {

        Configuration configuration = getConfiguration();

        configuration.getCommon().addEventType("Event", Event.class);
        configuration.getCommon().addEventType("Transaction", Transaction.class);
        configuration.getCommon().getEventMeta().setClassPropertyResolutionStyle(PropertyResolutionStyle.CASE_INSENSITIVE);
        configuration.getCommon().addImport("com.vusecurity.eventprocessing.utils.geo.*");

        runtime = EPRuntimeProvider.getDefaultRuntime(configuration);
        logger.info("EPRuntime created successfully");

        createStatements(listeners);
    }

    private void createStatements(List<EventProcessListener> listeners) {
        logger.info("Creating statements for {} listeners", listeners.size());

        // For testing...
        for (String deploymentId : runtime.getDeploymentService().getDeployments()) {
            EPDeployment deployment = runtime.getDeploymentService().getDeployment(deploymentId);
            for (EPStatement statement : deployment.getStatements()) {
                logger.debug("Existing statement: {} : {}", statement.getName(), statement.getProperty(StatementProperty.EPL));
            }
        }

        String contextStatement = null;

        for (EventProcessListener listener : listeners) {
            logger.debug("Processing listener for rule ID: {}", listener.getRuleId());

            String[] statements = listener.getQueryStatement().split(";");

            if (listener.getContextStatement() != null && !listener.getContextStatement().isEmpty()) {
                contextStatement = listener.getContextStatement();
                logger.debug("Context statement set: {}", contextStatement);
            }

            try {
                for (int i = 0; i < statements.length; i++) {

                    String statementName = String.format("Rule-{%d)-subrule{%d}", listener.getRuleId(), i);
                    logger.debug("Processing statement: {}", statementName);

                    boolean isDeployed = runtime.getDeploymentService().getStatement(statementName, statementName) != null;

                    if (isDeployed) {
                        logger.info("Undeploying existing statement: {}", statementName);
                        runtime.getDeploymentService().undeploy(statementName);
                    }

                    if (listener.getRule().isEnabled()) {

                        String statement = contextStatement != null ? contextStatement + ";\n" : "";
                        statement += String.format("@name('%s') %s;\n", statementName, statements[i]);

                        logger.debug("Compiling and deploying statement: {}", statement);

                        compileAndDeploy(statement, statementName);

                        EPStatement epStatement = runtime.getDeploymentService().getStatement(statementName, statementName);
                        epStatement.addListener(listener);
                        logger.info("Statement {} deployed and listener added successfully", statementName);
                    } else {
                        logger.info("Rule {} is disabled, skipping statement creation", listener.getRuleId());
                    }
                }
            } catch (Exception ex) {
                logger.error("An error occurred creating statements for rule ID: {}", listener.getRuleId(), ex);
                throw new RuntimeException(ex);
            }
        }
    }

    private void compileAndDeploy(String statement, String statementName) throws EPCompileException, EPDeployException {
        logger.debug("Compiling and deploying statement: {}", statementName);

        Configuration configuration = runtime.getConfigurationDeepCopy();
        CompilerArguments args = new CompilerArguments(configuration);
        args.getPath().add(runtime.getRuntimePath());

        EPCompiled compiled = EPCompilerProvider.getCompiler().compile(statement, args);

        DeploymentOptions deployOptions = new DeploymentOptions()
                .setDeploymentId(statementName);

        runtime.getDeploymentService().deploy(compiled, deployOptions);
        logger.info("Statement {} compiled and deployed successfully", statementName);
    }

    @PreDestroy
    public void destroy() {
        logger.info("Destroying EPRuntime");
        runtime.destroy();
    }
}
