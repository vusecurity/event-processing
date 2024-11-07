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

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import com.vusecurity.eventprocessing.models.RuleEventStatement;
import com.vusecurity.eventprocessing.models.RuleItem;
import com.vusecurity.eventprocessing.services.IEventProcessHandler;

import java.util.Set;

public class EventProcessListener implements UpdateListener {

    IEventProcessHandler parent;
    RuleItem rule;

    String contextStatement;
    String queryStatement;

    public EventProcessListener(EventProcessHandler eventProcessHandler, RuleItem rule, Set<RuleEventStatement> statements) {
        parent = eventProcessHandler;
        this.rule = rule;

        for (RuleEventStatement statement :
                statements) {

            if(statement.isContext())
                contextStatement = statement.getStatement();
            else
                queryStatement = statement.getStatement();
        }
    }

    public String getContextStatement() {
        return contextStatement;
    }

    public void setContextStatement(String contextStatement) {
        this.contextStatement = contextStatement;
    }

    public String getQueryStatement() {
        return queryStatement;
    }

    public void setQueryStatement(String queryStatement) {
        this.queryStatement = queryStatement;
    }

    @Override
    public void update(EventBean[] eventBeans, EventBean[] eventBeans1, EPStatement epStatement, EPRuntime epRuntime) {
        parent.triggered(rule.getId());
    }

    public int getRuleId() {
        return rule.getId();
    }

    public RuleItem getRule() {
        return rule;
    }
}
