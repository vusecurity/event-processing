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

package com.vusecurity.eventprocessing.viewmodel.request.events;

import com.vusecurity.eventprocessing.viewmodel.request.BaseRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeEvent extends BaseRequest {


    private int idChannel;
    private int idOperation;
    private String userId;
    private Date eventDate;

    private String ip;
    private Location deviceLocation;

    private BaseGeoData geoData;

    private String fingerprintData;

    private Map<String, String> aditionalParameters = new HashMap<String, String>();

    public int getIdChannel() {
        return idChannel;
    }

    public void setIdChannel(int idChannel) {
        this.idChannel = idChannel;
    }

    public int getIdOperation() {
        return idOperation;
    }

    public void setIdOperation(int idOperation) {
        this.idOperation = idOperation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Location getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(Location location) {
        this.deviceLocation = location;
    }

    public String getFingerprintData() {
        return fingerprintData;
    }

    public void setFingerprintData(String fingerprintData) {
        this.fingerprintData = fingerprintData;
    }

    public Map<String, String> getAditionalParameters() {
        return aditionalParameters;
    }

    public void setAditionalParameters(Map<String, String> aditionalParameters) {
        this.aditionalParameters = aditionalParameters;
    }

    public BaseGeoData getGeoData() {
        return geoData;
    }

    public void setGeoData(BaseGeoData geoData) {
        this.geoData = geoData;
    }
}
