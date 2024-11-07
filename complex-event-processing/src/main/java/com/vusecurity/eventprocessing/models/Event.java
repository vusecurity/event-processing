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
import com.vusecurity.eventprocessing.models.geo.DeviceLocation;
import com.vusecurity.eventprocessing.utils.BooleanParser;

import java.text.SimpleDateFormat;
import java.util.*;

public class Event implements IModel {

    private long id;
    private int idChannel;
    private int idOperation;
    private String userId;
    private Date eventDate;

    private BaseGeoData geoData;

    private boolean hasGeoData;

    private DeviceLocation deviceLocation;

    private Map<String, String> aditionalParameters = new HashMap<>();

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

    public Map<String, String> getAditionalParameters() {
        return aditionalParameters;
    }

    public void setAditionalParameters(Map<String, String> aditionalParameters) {
        this.aditionalParameters = aditionalParameters;
    }

    public void addAditionalParameter(String key, String value){
        this.aditionalParameters.put(key, value);
    }

    public int getHashId(){
        return getIdChannel() * 1000000 + getIdOperation();
    }

    @Override
    public boolean fill(List<String> parameters) {
        return false;
    }

    @Override
    public int getIntValue(String name) {
        if (name.compareTo("idCanal") == 0)
            return getIdChannel();

        if (name.compareTo("idTipoOperacion") == 0)
            return getIdOperation();

        if(getAditionalParameters().containsKey(name))
            return Integer.parseInt(getAditionalParameters().get(name));

        return 0;
    }

    @Override
    public long getLongValue(String name) {
        if(getAditionalParameters().containsKey(name))
            return Long.parseLong(getAditionalParameters().get(name));

        return 0;
    }

    @Override
    public double getDoubleValue(String name) {
        if(getAditionalParameters().containsKey(name))
            return Double.parseDouble(getAditionalParameters().get(name));

        return 0.0;
    }

    @Override
    public String getStringValue(String name) {
        if (name.compareTo("idUsuario") == 0)
            return getUserId();

        if (name.compareTo("hora") == 0)
            return new SimpleDateFormat("HH:mm").format(getEventDate());

        if (getAditionalParameters().containsKey(name))
            return getAditionalParameters().get(name);

        if(geoData != null){
            return geoData.getStringValue(name);
        }

        return null;
    }

    @Override
    public Date getDateTimeValue(String name) {

        if(name.compareTo("fechaHora") == 0)
            return getEventDate();

        return null;
    }

    @Override
    public boolean getBooleanValue(String name) {
        if(getAditionalParameters().containsKey(name)){

            return BooleanParser.normalizeBoolean(getAditionalParameters().get(name));
        }

        return false;
    }

    @Override
    public BaseGeoData getGeoDataValue(String name) {

        if(name.equals("Geolocalizacion"))
            return this.geoData;

        return null;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public void reset() {
        this.aditionalParameters.clear();
    }

    public int getChannelOperationHash(){
        return this.getIdChannel() * 1000000 + this.getIdOperation();
    }

    public void setChannelOperationHash(int value){

    }

    public boolean isHasGeoData() {
        return hasGeoData;
    }

    protected void setHasGeoData(boolean hasGeoData) {
        this.hasGeoData = hasGeoData;
    }

    public BaseGeoData getGeoData() {
        return geoData;
    }

    public void setGeoData(BaseGeoData geoData) {
        this.geoData = geoData;
    }

    public DeviceLocation getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(DeviceLocation deviceLocation) {
        this.deviceLocation = deviceLocation;

        if(deviceLocation != null)
            this.setHasGeoData(true);
    }
}
