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

package com.vusecurity.eventprocessing.mappers;

import com.vusecurity.eventprocessing.models.Event;
import com.vusecurity.eventprocessing.models.geo.BaseGeoData;
import com.vusecurity.eventprocessing.models.geo.DeviceLocation;
import com.vusecurity.eventprocessing.viewmodel.request.events.AnalyzeEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventMapper {

    public Event map(AnalyzeEvent request) {
        Event event = new Event();
        event.setIdChannel(request.getIdChannel());
        event.setIdOperation(request.getIdOperation());
        event.setUserId(request.getUserId());
        event.setEventDate(request.getEventDate());

        Optional.ofNullable(request.getDeviceLocation())
                .ifPresent(location -> event.setDeviceLocation(new DeviceLocation(
                        location.getLatitude(),
                        location.getLongitude()
                )));

        request.getAditionalParameters().forEach(event::addAditionalParameter);

        event.setGeoData(mapGeoData(request));

        return event;
    }

    private BaseGeoData mapGeoData(AnalyzeEvent request) {
        return Optional.ofNullable(request.getGeoData())
                .map(geoData -> {
                    BaseGeoData baseGeoData = new BaseGeoData();
                    baseGeoData.setCountry(geoData.getCountry());
                    baseGeoData.setCity(geoData.getCity());
                    baseGeoData.setProvinceOrState(geoData.getProvinceOrState());
                    baseGeoData.setLocality(geoData.getLocality());
                    return baseGeoData;
                })
                .orElse(new BaseGeoData());
    }
}