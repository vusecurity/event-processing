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

package com.vusecurity.eventprocessing.models.geo;

import com.vusecurity.eventprocessing.models.IModel;

import java.util.Date;
import java.util.List;

public class BaseGeoData implements IModel {
    private String country;
    private String city;
    private String provinceOrState;
    private String postalCode;
    private String locality;

    private boolean ruleTriggered;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvinceOrState() {
        return provinceOrState;
    }

    public void setProvinceOrState(String provinceOrState) {
        this.provinceOrState = provinceOrState;
    }

    public boolean isEmpty() {
        return  getCountry() == null
                && getCity() == null
                && getPostalCode() == null;
    }

    @Override
    public boolean fill(List<String> parameters) {
        return false;
    }

    @Override
    public int getIntValue(String name) {
        return 0;
    }

    @Override
    public long getLongValue(String name) {
        return 0;
    }

    @Override
    public double getDoubleValue(String name) {
        return 0;
    }

    @Override
    public String getStringValue(String name) {

        if (name.compareTo("Pais") == 0)
            return this.getCountry();

        if (name.compareTo("Ciudad") == 0)
            return this.getCity();

        if(name.compareTo("CodigoPostal") == 0)
            return this.getPostalCode();

        return null;
    }

    @Override
    public Date getDateTimeValue(String name) {
        return null;
    }

    @Override
    public boolean getBooleanValue(String name) {
        return false;
    }

    @Override
    public BaseGeoData getGeoDataValue(String name) {

        if(name.equals("Geolocalizacion"))
            return this;

        return null;
    }

    public boolean isRuleTriggered() {
        return ruleTriggered;
    }

    public void setRuleTriggered(boolean ruleTriggered) {
        this.ruleTriggered = ruleTriggered;
    }

    public String getLocality() {
        if(locality == null)
            return "NO_LOCALITY";
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        return "BaseGeoData{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", provinceOrState='" + provinceOrState + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
