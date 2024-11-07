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
import com.vusecurity.eventprocessing.models.Rule;
import com.vusecurity.eventprocessing.models.RuleItem;
import com.vusecurity.eventprocessing.models.RuleResult;
import com.vusecurity.eventprocessing.services.IRuleService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RuleCore {

    private static final Logger logger = LoggerFactory.getLogger(RuleCore.class);

    EventProcessHandler eventProcessHandler;
    IRuleService ruleService;

    private long lastKnownVersion;

    public RuleCore(EventProcessHandler eventProcessHandler, IRuleService ruleService) {
        this.eventProcessHandler = eventProcessHandler;
        this.ruleService = ruleService;
        this.lastKnownVersion = -1;
    }

    @PostConstruct
    public void initializeCache() {
        checkAndInitialize();
    }

    public RuleResult executeEventProcessing(Event trx) {
        checkAndInitialize();

        RuleResult result = new RuleResult();

        if (eventProcessHandler.handle(trx)) {
            Map<Integer, Rule> cachedRules = ruleService.getEventProcessingRulesMap();
            int triggeredRuleId = eventProcessHandler.getTriggeredRuleId();
            result.setTriggeredRule(cachedRules.get(triggeredRuleId));
        }

        return result;
    }

    private void checkAndInitialize() {
        long currentVersion = ruleService.getRulesVersion();

        if (currentVersion != lastKnownVersion) {
            logger.info("Rules version changed. Updating rules. Current: {}, Last known: {}", currentVersion, lastKnownVersion);
            List<RuleItem> updatedRules = ruleService.getEventProcessingRules();
            eventProcessHandler.initialize(updatedRules);
            lastKnownVersion = currentVersion;
        }
    }
}
