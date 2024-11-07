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

package com.vusecurity.eventprocessing.repositories;

import com.vusecurity.eventprocessing.models.RuleItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRuleRepository extends CrudRepository<RuleItem, Integer> {

    List<RuleItem> findByEnabledTrueAndIdRuleTypeOrderById(int idRuleType);
    List<RuleItem> findByIdRuleTypeOrderById(int idRuleType);
    List<RuleItem> findByIdRuleType(int idRuleType);
}
