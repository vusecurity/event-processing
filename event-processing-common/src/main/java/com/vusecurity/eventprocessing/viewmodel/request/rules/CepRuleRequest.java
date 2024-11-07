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

package com.vusecurity.eventprocessing.viewmodel.request.rules;

import com.vusecurity.eventprocessing.viewmodel.request.BaseRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CepRuleRequest extends BaseRequest {

    @NotNull
    @NotEmpty
    private String name;
    private boolean enabled;
    private int idAction;
    private String jsonFormat;
    private int idCriticalLevel;

    @Valid
    private List<EventStatement> eventStatements = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getIdAction() {
        return idAction;
    }

    public void setIdAction(int idAction) {
        this.idAction = idAction;
    }

    public String getJsonFormat() {
        return jsonFormat;
    }

    public void setJsonFormat(String jsonFormat) {
        this.jsonFormat = jsonFormat;
    }

    public int getIdCriticalLevel() {
        return idCriticalLevel;
    }

    public void setIdCriticalLevel(int idCriticalLevel) {
        this.idCriticalLevel = idCriticalLevel;
    }

    public List<EventStatement> getEventStatements() {
        return eventStatements;
    }

    public void setEventStatements(List<EventStatement> eventStatements) {
        this.eventStatements = eventStatements;
    }
}
