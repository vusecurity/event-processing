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

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esperha.runtime.client.ConfigurationHA;
import com.espertech.esperha.runtime.client.ConfigurationHADefaults;
import com.espertech.esperha.runtime.client.ConfigurationStoreEnvironment;
import com.vusecurity.eventprocessing.services.EsperhaJDBCProperties;
import com.vusecurity.eventprocessing.services.FraudSerdeFactory;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@ConditionalOnProperty(prefix= "application.esperha", name = "enabled")
public class EventProcessHAService extends BaseEventProcess {

    @Value("${application.config-file}")
    private String config_file;
    public String DEPLOYMENT_ID = "fraud-rules";

    EsperhaJDBCProperties jdbcProperties;

    public EventProcessHAService(EsperhaJDBCProperties jdbcProperties) {
        this.jdbcProperties = jdbcProperties;
    }


    private ConfigurationStoreEnvironment getConfigurationStoreEnvironmentForJdbc() {
        Properties props = new Properties();
        props.put("username", jdbcProperties.getUsername());
        props.put("password", jdbcProperties.getPassword());
        props.put("driverClassName", jdbcProperties.getDriver());
        props.put("url", jdbcProperties.getUrl());
        props.put("initialSize", 1);
        props.put("defaultAutoCommit", "false");

        ConfigurationStoreEnvironment.TargetJDBC targetJDBC = new ConfigurationStoreEnvironment.TargetJDBC();
        targetJDBC.setDatabaseType(jdbcProperties.getDatabaseType());
        targetJDBC.setDataSourceFactory(props, BasicDataSourceFactory.class.getName());

        ConfigurationStoreEnvironment env = new ConfigurationStoreEnvironment();
        env.getTargets().put("jdbc-one", targetJDBC);
        env.setStateManagement(new ConfigurationStoreEnvironment.StateManagementPassive());
        env.setTargetName("jdbc-one");
        return env;
    }

    @Override
    protected Configuration getConfiguration() {
        ConfigurationStoreEnvironment env = getConfigurationStoreEnvironmentForJdbc();

        ConfigurationHA configuration = new ConfigurationHA();
        configuration.getStoreEnvironments().put(
                ConfigurationHADefaults.DEFAULT_STORE_NAME, env);

        configuration.getCompiler().getSerde().addSerdeProviderFactory(FraudSerdeFactory.class.getName());

        return configuration;
    }
}
