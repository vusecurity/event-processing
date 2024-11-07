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

import com.vusecurity.eventprocessing.models.Event;
import com.vusecurity.eventprocessing.models.RuleItem;
import com.vusecurity.eventprocessing.services.IEventProcessHandler;
import com.vusecurity.eventprocessing.services.IEventProcessService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventProcessHandler implements IEventProcessHandler {

    IEventProcessService eventProcessService;
    List<EventProcessListener> listeners;

    public EventProcessHandler(IEventProcessService service) {
        this.eventProcessService = service;
    }

    boolean triggered = false;
    int triggeredRuleId = 0;

    @Override
    public boolean handle(Event event) {
        triggered = false;

        eventProcessService.sendEvent(event);

        return triggered;
    }

    @Override
    public void triggered(int ruleId) {
        triggeredRuleId = ruleId;
        triggered = true;
    }

    @Override
    public int getTriggeredRuleId() {
        return triggeredRuleId;
    }

    @Override
    public void initialize(List<RuleItem> eventRules) {

        listeners = new ArrayList<>();

        for (RuleItem ruleItem :
                eventRules) {

            listeners.add(new EventProcessListener(this, ruleItem, ruleItem.getRuleEventStatements()));
        }

        eventProcessService.initialize(listeners);
    }
}
