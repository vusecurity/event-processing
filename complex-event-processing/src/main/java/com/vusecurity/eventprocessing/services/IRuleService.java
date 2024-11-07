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

package com.vusecurity.eventprocessing.services;

import com.vusecurity.eventprocessing.models.Rule;
import com.vusecurity.eventprocessing.models.RuleItem;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IRuleService {

    List<RuleItem> getEventProcessingRules();
    List<RuleItem> getAllEventProcessingRules();
    Map<Integer, Rule> getEventProcessingRulesMap();
    Optional<RuleItem> getById(int id);
    RuleItem save(RuleItem rule);
    RuleItem update(RuleItem rule);
    boolean delete(int idRule);
    long getRulesVersion();
}
