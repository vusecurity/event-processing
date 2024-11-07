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

package com.vusecurity.eventprocessing.models.rules;

import com.vusecurity.eventprocessing.models.Event;
import com.vusecurity.eventprocessing.models.Rule;
import com.vusecurity.eventprocessing.services.IEventProcessHandler;

public class EventProcessingRule extends Rule {
    IEventProcessHandler handler;

    public EventProcessingRule(IEventProcessHandler handler){
        this.handler = handler;
    }

    @Override
    public boolean validate() {
        return handler.handle((Event) getModel());
    }

    public void setHandler(IEventProcessHandler handler) {
        this.handler = handler;
    }
}
