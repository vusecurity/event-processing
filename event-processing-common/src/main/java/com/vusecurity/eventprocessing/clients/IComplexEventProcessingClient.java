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

package com.vusecurity.eventprocessing.clients;

import com.vusecurity.eventprocessing.viewmodel.request.events.AnalyzeEvent;
import com.vusecurity.eventprocessing.viewmodel.request.rules.CepRuleRequest;
import com.vusecurity.eventprocessing.viewmodel.response.AnalyzeResult;
import com.vusecurity.eventprocessing.viewmodel.response.BaseResponse;
import com.vusecurity.eventprocessing.viewmodel.response.CepRuleResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

public interface IComplexEventProcessingClient {

    BaseResponse save(@RequestBody CepRuleRequest rule);
    BaseResponse update(@RequestBody CepRuleRequest rule, @PathVariable(value = "id") int id);
    BaseResponse delete(@PathVariable int id);
    List<CepRuleResponse> get();
    CepRuleResponse getId(int id);
    AnalyzeResult sendEvent(@RequestBody AnalyzeEvent event);
}
