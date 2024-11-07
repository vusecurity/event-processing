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


import com.vusecurity.eventprocessing.models.rules.IRuleValidate;

public abstract class Rule implements IRuleValidate {

    private int id;
    private String description;
    private int score;
    private int idChannel;
    private int idOperationType;
    private int idAction;
    private Probability probability;
    private Impact impact;
    private int idBusinessGroup;
    private int idBusinessUser;

    private IModel model;
    private IModel lastTransaction;
    private IModel geoData;
    private IModel lastEvent;

   // private User user;
    private int idRuleType;

    public void setIdRuleType(int idRuleType) {
        this.idRuleType = idRuleType;
    }

    public int getIdRuleType(){ return this.idRuleType; }

    public enum Action{
        NONE,
        ALLOW,
        AUTH,
        DENY,
        REVIEW,
        BLOCK,
        CASE
    }

    public enum RuleType{
        None,
        Operacional,
        Geoposicional,
        Fingerprint,
        FingerprintAccuracy,
        EventProcessing,
        Risk,
        Cross,
        AssignCase
    }

    public enum Probability{
        None,
        MuyAlto,
        Alto,
        Medio,
        Bajo,
        MuyBajo
    }

    public enum Impact{
        None,
        Insignificante,
        Menor,
        Moderado,
        Significante,
        Severo
    }

    public IModel getModel() {
        return model;
    }

    public void setModel(IModel model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public IModel getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(IModel lastTransaction) {
        this.lastTransaction = lastTransaction;
    }

    public IModel getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(IModel lastEvent) {
        this.lastEvent = lastEvent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAction() {
        return idAction;
    }

    public void setIdAction(int idAction) {
        this.idAction = idAction;
    }

    public Probability getProbability() {
        return probability;
    }

    public void setProbability(Probability probability) {
        this.probability = probability;
    }

    public Impact getImpact() {
        return impact;
    }

    public void setImpact(Impact impact) {
        this.impact = impact;
    }

    public IModel getGeoData() {
        return geoData;
    }

    public void setGeoData(IModel geoData) {
        this.geoData = geoData;
    }

    public int getIdBusinessGroup() {
        return idBusinessGroup;
    }

    public void setIdBusinessGroup(int idBusinessGroup) {
        this.idBusinessGroup = idBusinessGroup;
    }

    public int getIdBusinessUser() {
        return idBusinessUser;
    }

    public void setIdBusinessUser(int idBusinessUser) {
        this.idBusinessUser = idBusinessUser;
    }
}
