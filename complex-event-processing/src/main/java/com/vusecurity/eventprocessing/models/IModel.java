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


import com.vusecurity.eventprocessing.models.geo.BaseGeoData;

import java.util.Date;
import java.util.List;

public interface IModel {

    public boolean fill(List<String> parameters);

    public int getIntValue(String name);
    public long getLongValue(String name);
    public double getDoubleValue(String name);
    public String getStringValue(String name);
    public Date getDateTimeValue(String name);
    public boolean getBooleanValue(String name);
    BaseGeoData getGeoDataValue(String name);
}
