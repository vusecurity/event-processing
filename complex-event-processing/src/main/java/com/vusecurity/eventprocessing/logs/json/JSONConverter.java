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

package com.vusecurity.eventprocessing.logs.json;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.pattern.CompositeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vusecurity.eventprocessing.logs.LogstashPayload;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JSONConverter extends CompositeConverter<ILoggingEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JSONConverter() {
    }

    protected String transform(ILoggingEvent event, String in) {
        try {
            if (event instanceof LoggingEvent) {
                if (event.getThrowableProxy() instanceof ThrowableProxy) {
                    ThrowableProxy throwableProxy = (ThrowableProxy) event.getThrowableProxy();
                    Throwable throwable = throwableProxy.getThrowable();
                    if (throwable instanceof Exception) {
                        HashMap<String, Object> errorLog = new HashMap<>();
                        errorLog.put("event", event.getMessage());
                        errorLog.put("message", throwable.getMessage());
                        errorLog.put("class", throwable.getClass());

                        StackTraceElement[] fullStackTrace = throwable.getStackTrace();
                        List<StackTraceElement> filteredAndLimitedStackTrace = Arrays.stream(fullStackTrace)
                                .filter(element -> element.getClassName().contains("vu.") || element.getClassName().contains(".vusecurity"))
                                .limit(20)
                                .collect(Collectors.toList());

                        errorLog.put("stackTrace", filteredAndLimitedStackTrace);

                        errorLog.put("cause", throwable.getCause());

                        in = objectMapper.writeValueAsString(errorLog);
                    }
                }
            }
            if (!isJSONValid(in) ) {
                LogstashPayload payload = new LogstashPayload();
                payload.setBody(in);
                in = objectMapper.writeValueAsString(payload);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return in;
    }

    public boolean isJSONValid(String jsonInString ) {
        try {
            objectMapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
