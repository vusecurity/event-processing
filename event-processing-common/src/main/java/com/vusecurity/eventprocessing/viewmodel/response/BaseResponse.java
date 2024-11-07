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

package com.vusecurity.eventprocessing.viewmodel.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private int code;
    private String message;
    private String traceId;

    private T data;

    public BaseResponse() {
    }

    public BaseResponse(int code, String message, String traceId) {
        this.code = code;
        this.message = message;
        this.traceId = traceId;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}