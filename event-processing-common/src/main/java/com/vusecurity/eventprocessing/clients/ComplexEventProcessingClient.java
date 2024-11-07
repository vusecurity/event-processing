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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class ComplexEventProcessingClient implements IComplexEventProcessingClient{

    RestTemplate restTemplate;

    public ComplexEventProcessingClient() {
        this.restTemplate = new RestTemplate();
    }

    private String domain;

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public BaseResponse save(CepRuleRequest rule) {

        String url = domain + "/cep/rules";

        HttpHeaders requestHeaders = getHttpHeaders();
        HttpEntity<CepRuleRequest> request = new HttpEntity<>(rule, requestHeaders);
        ResponseEntity<BaseResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, BaseResponse.class);

        return response.getBody();
    }

    @Override
    public BaseResponse update(CepRuleRequest rule, int id) {
        String url = domain + "/cep/rules/{id}";

        HttpHeaders requestHeaders = getHttpHeaders();

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(id);

        HttpEntity<CepRuleRequest> request = new HttpEntity<>(rule, requestHeaders);
        ResponseEntity<BaseResponse> response = restTemplate.exchange(uri.toUriString(), HttpMethod.PATCH, request, BaseResponse.class);

        return response.getBody();
    }

    @Override
    public BaseResponse delete(int id) {
        String url = domain + "/cep/rules/{id}";

        HttpHeaders requestHeaders = getHttpHeaders();

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(id);

        HttpEntity<CepRuleRequest> request = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<BaseResponse> response = restTemplate.exchange(uri.toUriString(), HttpMethod.DELETE, request, BaseResponse.class);

        return response.getBody();
    }

    @Override
    public List<CepRuleResponse> get() {
        String url = domain + "/cep/rules";

        HttpHeaders requestHeaders = getHttpHeaders();

        ResponseEntity<List<CepRuleResponse>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), new ParameterizedTypeReference<List<CepRuleResponse>>(){});

        return response.getBody();
    }

    @Override
    public CepRuleResponse getId(int id) {
        String url = domain + "/cep/rules/{id}";

        HttpHeaders requestHeaders = getHttpHeaders();

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(id);

        HttpEntity<CepRuleRequest> request = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<CepRuleResponse> response = restTemplate.exchange(uri.toUriString(), HttpMethod.GET, request, CepRuleResponse.class);

        return response.getBody();
    }

    @Override
    public AnalyzeResult sendEvent(AnalyzeEvent event) {
        String url = domain + "/cep/event";

        HttpHeaders requestHeaders = getHttpHeaders();
        HttpEntity<AnalyzeEvent> request = new HttpEntity<>(event, requestHeaders);
        ResponseEntity<AnalyzeResult> response = restTemplate.exchange(url, HttpMethod.POST, request, AnalyzeResult.class);

        return response.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }
}
