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

package com.vusecurity.eventprocessing.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vusecurity.eventprocessing.logs.json.MaskSensitiveDataAnnotationIntrospector;
import com.vusecurity.eventprocessing.viewmodel.request.BaseRequest;
import com.vusecurity.eventprocessing.viewmodel.response.BaseResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;


@Aspect
@Component
public class LogstashAspect {

    private Logger logger;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public LogstashAspect() {
        AnnotationIntrospector sis = objectMapper.getSerializationConfig().getAnnotationIntrospector();
        AnnotationIntrospector is1 = AnnotationIntrospectorPair.pair(sis, new MaskSensitiveDataAnnotationIntrospector());

        objectMapper.setAnnotationIntrospector(is1);
    }

    @AfterThrowing(value = "@annotation(Logstash)", throwing = "exception")
    public void afterReturning(JoinPoint joinPoint, Exception exception) throws JsonProcessingException {
        logger = getLogger(joinPoint);

        HttpServletRequest request = currentRequest();

        LogstashPayload payload = new LogstashPayload();
        payload.setVerb(request.getMethod());
        payload.setPath(request.getRequestURI());
        payload.setAction("Error");

        StringBuilder sb = new StringBuilder();

        if(exception.getCause() != null)
            sb.append("Exception: ").append(exception.getCause().toString());

        StackTraceElement stackTraceElement = exception.getStackTrace()[0];

        sb.append("Class: ").append(stackTraceElement.getClassName())
                .append(" FileName: ").append(stackTraceElement.getFileName())
                .append( " MethodName: ").append(stackTraceElement.getMethodName())
                .append( " lineNumber: ").append(stackTraceElement.getLineNumber());

        payload.setException(sb.toString());

        if(exception instanceof CodeException){
            CodeException ex = (CodeException) exception;
            payload.setBody(
                    objectMapper.writeValueAsString(new BaseResponse(ex.getCode(), ex.getMessage(), MDC.get("traceId")))
            );
        }

        MDC.put("httpMessageType", "RESPONSE");
        MDC.put("http_status", "400");

        logger.error(filterBody(payload));

        MDC.remove("http_status");
    }

    @Around(value = "@annotation(Logstash)")
    public Object logstashAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        logger = getLogger(joinPoint);

        HttpServletRequest request = currentRequest();

        String requestUri;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Logstash logstash = method.getAnnotation(Logstash.class);

        if (logstash.exceptionsOnly()) {
            return joinPoint.proceed(); // Skip logging if exceptionsOnly is true
        }

        if (logstash.maskPath())
            requestUri = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/") + 1) + "****";
        else
            requestUri = request.getRequestURI();

        String srvRef = String.format("[%s] %s", request.getMethod(), requestUri);

        MDC.put("srvRef", srvRef);
        MDC.put("httpMessageType", "REQUEST");

        Object[] params = joinPoint.getArgs();

        BaseRequest body = null;

        for (Object o : params) {
            if( o instanceof BaseRequest)
                body = (BaseRequest)o;
        }

        MDC.put("operationId", method.getName());

        LogstashPayload payload = new LogstashPayload();
        payload.setVerb(request.getMethod());
        payload.setPath(requestUri);
        payload.setAction("Begin");

        payload.setBody(body);

        logger.info(filterBody(payload));
        MDC.put("httpMessageType", "-");
        Object returnValue = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        MDC.put("timeResponse", String.valueOf(executionTime));
        MDC.put("httpMessageType", "RESPONSE");
        MDC.put("http_status", "200");

        payload = new LogstashPayload();
        payload.setVerb(request.getMethod());
        payload.setPath(requestUri);
        payload.setAction("End");
        payload.setBody(returnValue);

        logger.info(filterBody(payload));

        MDC.remove("http_status");
        MDC.remove("timeResponse");

        return returnValue;
    }

    private String filterBody(Object object) throws JsonProcessingException {
        String body = objectMapper.writeValueAsString(object);
        body = body.replaceAll("data:image[^\"]*","<mask>");
        return body;
    }


    private Logger getLogger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType().getName());
    }

    private HttpServletRequest currentRequest() {
        // Use getRequestAttributes because of its return null if none bound
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(servletRequestAttributes).map(ServletRequestAttributes::getRequest).orElse(null);
    }

}
