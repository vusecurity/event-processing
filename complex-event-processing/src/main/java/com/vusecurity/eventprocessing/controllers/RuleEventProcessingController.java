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

import com.vusecurity.eventprocessing.models.RuleItem;
import com.vusecurity.eventprocessing.services.IRuleService;
import com.vusecurity.eventprocessing.mappers.RuleMapper;
import com.vusecurity.eventprocessing.viewmodel.request.rules.CepRuleRequest;
import com.vusecurity.eventprocessing.viewmodel.response.BaseResponse;
import com.vusecurity.eventprocessing.viewmodel.response.CepRuleResponse;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("/cep/rules")
@RestController
public class RuleEventProcessingController {
    IRuleService ruleService;
    RuleMapper ruleMapper;

    public RuleEventProcessingController(IRuleService ruleService, RuleMapper ruleMapper) {
        this.ruleService = ruleService;
        this.ruleMapper = ruleMapper;
    }

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public BaseResponse save(@RequestBody @Valid CepRuleRequest rule){

        RuleItem created = ruleService.save(ruleMapper.map(rule));

        CepRuleResponse reponse = ruleMapper.mapToResponse(created);

        BaseResponse<CepRuleResponse> baseResponse = new BaseResponse(10201, "Rule created successfully", MDC.get("traceId"));

        baseResponse.setData(reponse);

        return baseResponse;
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public BaseResponse update(@RequestBody @Valid CepRuleRequest rule, @PathVariable(value = "id") int id){

        RuleItem ruleItem = ruleMapper.map(rule);
        ruleItem.setId(id);

        ruleService.update(ruleItem);

        return new BaseResponse(10202, "Rule updated successfully", MDC.get("traceId"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> delete(@PathVariable int id) {
        boolean wasDeleted = ruleService.delete(id);

        if (wasDeleted) {
            return new BaseResponse<>(10203, "Rule deleted successfully", MDC.get("traceId"));
        } else {
            return new BaseResponse<>(10204, "Rule not found, no action taken", MDC.get("traceId"));
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CepRuleResponse> getAll() {
        return ruleService.getAllEventProcessingRules().stream()
                .map(ruleMapper::mapToResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public CepRuleResponse getById(@PathVariable int id){

        Optional<RuleItem> ruleItem = ruleService.getById(id);

        CepRuleResponse ruleResponse = null;

        if(ruleItem.isPresent()){

            ruleResponse = ruleMapper.mapToResponse(ruleItem.get());
        }

        return ruleResponse;
    }
}
