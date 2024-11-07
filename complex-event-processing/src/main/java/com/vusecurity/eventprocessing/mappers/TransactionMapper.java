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

import com.vusecurity.eventprocessing.models.Transaction;
import com.vusecurity.eventprocessing.models.geo.BaseGeoData;
import com.vusecurity.eventprocessing.models.geo.DeviceLocation;
import com.vusecurity.eventprocessing.viewmodel.request.events.AnalyzeTransaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionMapper {

    public Transaction map(AnalyzeTransaction request) {
        Transaction transaction = new Transaction();
        transaction.setIdChannel(request.getIdChannel());
        transaction.setIdOperation(request.getIdOperation());
        transaction.setUserId(request.getUserId());
        transaction.setEventDate(request.getEventDate());
        transaction.setAmount(request.getAmount());
        transaction.setCreditAccount(request.getCreditAccount());
        transaction.setDebitAccount(request.getDebitAccount());

        Optional.ofNullable(request.getDeviceLocation())
                .ifPresent(location -> transaction.setDeviceLocation(new DeviceLocation(
                        location.getLatitude(),
                        location.getLongitude()
                )));

        request.getAditionalParameters().forEach(transaction::addAditionalParameter);

        transaction.setGeoData(mapGeoData(request));

        return transaction;
    }

    private BaseGeoData mapGeoData(AnalyzeTransaction request) {
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