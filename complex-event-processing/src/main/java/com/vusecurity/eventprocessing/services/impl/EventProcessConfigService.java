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
import com.espertech.esper.common.client.util.PropertyResolutionStyle;
import com.espertech.esper.compiler.client.CompilerArguments;
import com.espertech.esper.compiler.client.EPCompileException;
import com.espertech.esper.compiler.client.EPCompilerProvider;
import com.vusecurity.eventprocessing.models.Event;
import com.vusecurity.eventprocessing.models.Transaction;
import com.vusecurity.eventprocessing.models.cep.ResultQueryEPL;
import com.vusecurity.eventprocessing.services.IEventProcessConfigService;
import com.vusecurity.eventprocessing.services.exceptions.StatementCompileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EventProcessConfigService implements IEventProcessConfigService {

    private final Logger logger = LoggerFactory.getLogger(EventProcessConfigService.class);

    @Override
    public ResultQueryEPL checkStatementSyntax(String query) {
        ResultQueryEPL result = new ResultQueryEPL();

        Configuration configuration = new Configuration();

        configuration.getCommon().addEventType("Event", Event.class);
        configuration.getCommon().addEventType("Transaction", Transaction.class);
        configuration.getCommon().getEventMeta().setClassPropertyResolutionStyle(PropertyResolutionStyle.CASE_INSENSITIVE);
        configuration.getCommon().addImport("com.vusecurity.eventprocessing.utils.geo.*");

        CompilerArguments args = new CompilerArguments(configuration);
        try {
            EPCompilerProvider.getCompiler().compile(query, args);
            result.setValid(true);
        } catch (EPCompileException e) {
            result.setValid(false);
            result.setErrorMessage(e.getMessage());
            logger.error("failed to compile statement", e);
            throw new StatementCompileException(e);
        }

        return result;
    }
}
