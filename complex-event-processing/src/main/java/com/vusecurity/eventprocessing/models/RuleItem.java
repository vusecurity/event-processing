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

package com.vusecurity.eventprocessing.models;

import jakarta.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "rule_event")
public class RuleItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RuleEventStatement> ruleEventStatements = new HashSet<>();

    @Id
    @SequenceGenerator(name = "rule_id_seq", sequenceName = "rule_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "rule_id_seq")
    private int id;
    private String name;
    private boolean enabled;
    private int idRuleType;
    private int score;
    private int idChannel;
    private int idOperationType;
    private int idAction;
    private int idCriticalLevel;
    @Size(max = 4000)
    private String jsonFormat;
    private LocalDateTime lastUpdateDate;

    public Set<RuleEventStatement> getRuleEventStatements() {
        return ruleEventStatements;
    }

    public void setRuleEventStatements(Set<RuleEventStatement> ruleEventStatements) {
        this.ruleEventStatements = ruleEventStatements;
    }

    public void addEventStatement(RuleEventStatement eventStatement){
        this.ruleEventStatements.add(eventStatement);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getIdRuleType() {
        return idRuleType;
    }

    public void setIdRuleType(int idRuleType) {
        this.idRuleType = idRuleType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public int getIdOperationType() {
        return idOperationType;
    }

    public void setIdOperationType(int idOperationType) {
        this.idOperationType = idOperationType;
    }

    public int getIdAction() {
        return idAction;
    }

    public void setIdAction(int idAction) {
        this.idAction = idAction;
    }

    public int getIdCriticalLevel() {
        return idCriticalLevel;
    }

    public void setIdCriticalLevel(int idCriticalLevel) {
        this.idCriticalLevel = idCriticalLevel;
    }

    public String getJsonFormat() {
        return jsonFormat;
    }

    public void setJsonFormat(String jsonFormat) {
        this.jsonFormat = jsonFormat;
    }

    private void addStatement(RuleEventStatement statement){
        this.ruleEventStatements.add(statement);
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
