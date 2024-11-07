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

import com.vusecurity.eventprocessing.models.Rule;
import com.vusecurity.eventprocessing.models.RuleEventStatement;
import com.vusecurity.eventprocessing.models.RuleItem;
import com.vusecurity.eventprocessing.models.rules.EventProcessingRule;
import com.vusecurity.eventprocessing.repositories.IRuleRepository;
import com.vusecurity.eventprocessing.services.IEventProcessConfigService;
import com.vusecurity.eventprocessing.services.IRuleService;
import com.vusecurity.eventprocessing.services.exceptions.RuleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
@CacheConfig(cacheNames = "rules")
public class RuleService implements IRuleService {

    private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

    IRuleRepository repository;
    EventProcessHandler eventProcessHandler;
    IEventProcessConfigService eventProcessConfigService;

    private final AtomicLong rulesVersion = new AtomicLong(0);

    private static final int EVENT_PROCESSING_RULE_TYPE = 5;

    @Autowired
    public RuleService(IRuleRepository repository,
                       EventProcessHandler eventProcessHandler,
                       IEventProcessConfigService eventProcessConfigService) {
        this.repository = repository;
        this.eventProcessHandler = eventProcessHandler;
        this.eventProcessConfigService = eventProcessConfigService;
    }

    @Override
    @Cacheable(key = "'ruleItemList'")
    public List<RuleItem> getEventProcessingRules() {
        return repository.findByIdRuleTypeOrderById(EVENT_PROCESSING_RULE_TYPE);
    }

    @Override
    @Cacheable(key = "'ruleItemList'")
    public List<RuleItem> getAllEventProcessingRules() {
        return repository.findByIdRuleTypeOrderById(EVENT_PROCESSING_RULE_TYPE);
    }

    @Override
    public Map<Integer, Rule> getEventProcessingRulesMap() {
        return mapToMap(getEventProcessingRules());
    }

    @Override
    @Cacheable(key = "#id")
    public Optional<RuleItem> getById(int id) {
        return repository.findById(id);
    }

    @Override
    @Caching(
            put = @CachePut(key = "#result.id"),
            evict = @CacheEvict(key = "'ruleItemList'")
    )
    public RuleItem save(RuleItem rule) {

        validate(rule);
        RuleItem savedRule = repository.save(rule);
        rulesVersion.incrementAndGet();
        return savedRule;
    }

    @Override
    @Caching(
            put = @CachePut(key = "#result.id"),
            evict = @CacheEvict(key = "'ruleItemList'")
    )
    public RuleItem update(RuleItem rule) {

        Optional<RuleItem> toUpdateOpt = repository.findById(rule.getId());

        if (toUpdateOpt.isEmpty()) {
            throw new RuleNotFoundException();
        }

        validate(rule);

        RuleItem toUpdate = toUpdateOpt.get();

        toUpdate.setJsonFormat(rule.getJsonFormat());
        toUpdate.setIdCriticalLevel(rule.getIdCriticalLevel());
        toUpdate.setIdRuleType(rule.getIdRuleType());
        toUpdate.setName(rule.getName());
        toUpdate.setEnabled(rule.isEnabled());
        toUpdate.setIdAction(rule.getIdAction());

        toUpdate.getRuleEventStatements().clear();

        for (RuleEventStatement statement : rule.getRuleEventStatements()) {

            RuleEventStatement newStatement = new RuleEventStatement(statement.getStatement(), statement.isContext());
            newStatement.setRule(toUpdate);
            toUpdate.addEventStatement(newStatement);
        }

        toUpdate.setLastUpdateDate(LocalDateTime.now());

        RuleItem savedRule = repository.save(toUpdate);
        rulesVersion.incrementAndGet();

        return savedRule;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#idRule"),
            @CacheEvict(key = "'ruleItemList'")
    })
    public boolean delete(int idRule) {
        Optional<RuleItem> toDelete = repository.findById(idRule);

        if (toDelete.isEmpty()) {
            logger.warn("Attempted to delete non-existent rule with ID: {}. No action taken.", idRule);
            return false;
        }

        repository.delete(toDelete.get());
        rulesVersion.incrementAndGet();
        logger.info("Rule with ID: {} has been successfully deleted.", idRule);
        return true;
    }

    public long getRulesVersion() {
        return rulesVersion.get();
    }

    private void validate(RuleItem item) {
        String context = item.getRuleEventStatements().stream()
                .filter(RuleEventStatement::isContext)
                .map(RuleEventStatement::getStatement)
                .findFirst()
                .map(stmt -> stmt + ";\n")
                .orElse("");

        String checkStatement = item.getRuleEventStatements().stream()
                .filter(stmt -> !stmt.isContext())
                .map(RuleEventStatement::getStatement)
                .findFirst()
                .orElse("");

        String statement = context + checkStatement + ";";

        eventProcessConfigService.checkStatementSyntax(statement);
    }

    private Map<Integer, Rule> mapToMap(List<RuleItem> rules) {
        Map<Integer, Rule> result = new HashMap<>();

        for (RuleItem ruleItem : rules) {
            Rule rule = new EventProcessingRule(eventProcessHandler);

            rule.setId(ruleItem.getId());
            rule.setScore(ruleItem.getScore());
            rule.setDescription(ruleItem.getName());
            rule.setIdChannel(ruleItem.getIdChannel());
            rule.setIdOperationType(ruleItem.getIdOperationType());
            rule.setIdAction(ruleItem.getIdAction());
            rule.setIdRuleType(ruleItem.getIdRuleType());

            result.put(ruleItem.getId(), rule);
        }

        return result;
    }
}
