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

package com.vusecurity.eventprocessing.controllers;

import com.vusecurity.eventprocessing.mappers.EventMapper;
import com.vusecurity.eventprocessing.mappers.TransactionMapper;
import com.vusecurity.eventprocessing.models.Event;
import com.vusecurity.eventprocessing.models.RuleResult;
import com.vusecurity.eventprocessing.models.Transaction;
import com.vusecurity.eventprocessing.services.impl.RuleCore;
import com.vusecurity.eventprocessing.viewmodel.request.events.AnalyzeEvent;
import com.vusecurity.eventprocessing.viewmodel.request.events.AnalyzeTransaction;
import com.vusecurity.eventprocessing.viewmodel.response.AnalyzeResult;
import com.vusecurity.eventprocessing.viewmodel.response.TriggeredRule;
import org.slf4j.MDC;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cep")
public class EventProcessingController {

    private static final int SUCCESS_CODE = 10200;
    private static final String SUCCESS_MESSAGE = "Analyze event finished";

    private final RuleCore ruleCore;
    private final EventMapper eventMapper;
    private final TransactionMapper transactionMapper;

    public EventProcessingController(RuleCore ruleCore, EventMapper eventMapper, TransactionMapper transactionMapper) {
        this.ruleCore = ruleCore;
        this.eventMapper = eventMapper;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping("/event")
    public AnalyzeResult sendEvent(@RequestBody @Validated AnalyzeEvent event) {
        Event eventToAnalyze = eventMapper.map(event);
        return processEvent(eventToAnalyze);
    }

    @PostMapping("/transaction")
    public AnalyzeResult sendTransaction(@RequestBody @Validated AnalyzeTransaction event) {
        Transaction transactionToAnalyze = transactionMapper.map(event);
        return processEvent(transactionToAnalyze);
    }

    private AnalyzeResult processEvent(Event event) {
        try {
            RuleResult result = ruleCore.executeEventProcessing(event);
            return createAnalyzeResult(result);
        } catch (Exception e) {
            return createErrorResult(e.getMessage());
        }
    }

    private AnalyzeResult createAnalyzeResult(RuleResult result) {
        AnalyzeResult response = new AnalyzeResult();
        response.setCode(SUCCESS_CODE);
        response.setMessage(SUCCESS_MESSAGE);
        response.setTraceId(MDC.get("traceId"));
        if (result.getTriggeredRule() != null) {
            TriggeredRule triggeredRule = new TriggeredRule();
            triggeredRule.setId(result.getTriggeredRule().getId());
            triggeredRule.setDescription(result.getTriggeredRule().getDescription());
            triggeredRule.setActionId(result.getTriggeredRule().getIdAction());
            response.setTriggeredRule(triggeredRule);
        }
        return response;
    }

    private AnalyzeResult createErrorResult(String errorMessage) {
        AnalyzeResult response = new AnalyzeResult();
        response.setCode(400);
        response.setMessage(errorMessage);
        response.setTraceId(MDC.get("traceId"));
        return response;
    }

}
