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

package com.vusecurity.eventprocessing.mappers;

import com.vusecurity.eventprocessing.models.RuleEventStatement;
import com.vusecurity.eventprocessing.models.RuleItem;
import com.vusecurity.eventprocessing.viewmodel.request.rules.EventStatement;
import com.vusecurity.eventprocessing.viewmodel.request.rules.CepRuleRequest;
import com.vusecurity.eventprocessing.viewmodel.response.CepRuleResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleMapper {

    private final int ID_RULE_TYPE = 5;

    public RuleItem map(CepRuleRequest rule){

        RuleItem ruleItem = new RuleItem();

        ruleItem.setEnabled(rule.isEnabled());
        ruleItem.setIdAction(rule.getIdAction());
        ruleItem.setName(rule.getName());
        ruleItem.setIdRuleType(ID_RULE_TYPE);
        ruleItem.setIdCriticalLevel(rule.getIdCriticalLevel());
        ruleItem.setJsonFormat(rule.getJsonFormat());

        for (EventStatement statement:
             rule.getEventStatements()) {

            RuleEventStatement newStatement = new RuleEventStatement(statement.getStatement(), statement.isContext());
            newStatement.setRule(ruleItem);
            newStatement.setId(statement.getId());
            ruleItem.addEventStatement(newStatement);

        }

        return ruleItem;
    }

    public CepRuleResponse mapToResponse(RuleItem ruleItem) {
        CepRuleResponse ruleResponse;

        ruleResponse = new CepRuleResponse();
        ruleResponse.setId(ruleItem.getId());
        ruleResponse.setEnabled(ruleItem.isEnabled());
        ruleResponse.setIdAction(ruleItem.getIdAction());
        ruleResponse.setIdCriticalLevel(ruleItem.getIdCriticalLevel());
        ruleResponse.setJsonFormat(ruleItem.getJsonFormat());
        ruleResponse.setName(ruleItem.getName());

        List<EventStatement> eventStatementList = new ArrayList<>();

        for (RuleEventStatement res :
                ruleItem.getRuleEventStatements()) {

            EventStatement es = new EventStatement();
            es.setContext(res.isContext());
            es.setStatement(res.getStatement());

            eventStatementList.add(es);
        }

        ruleResponse.setEventStatements(eventStatementList);
        return ruleResponse;
    }
}
