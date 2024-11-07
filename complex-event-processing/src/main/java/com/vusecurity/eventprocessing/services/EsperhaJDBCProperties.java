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

import com.espertech.esperha.runtime.client.ConfigurationStoreEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@ConfigurationProperties(prefix = "application.esper-ha-jdbc-target")
public class EsperhaJDBCProperties {

    private String platform;
    private String url;
    private String username;
    private String password;

    public String getPlatform() {
        return platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.platform = getPlatformFromUrl(url);
    }

    private String getPlatformFromUrl(String url) {
        String platform = "";

        String pattern = "jdbc:(\\w+):";
        Pattern regex = Pattern.compile(pattern);

        Matcher matcher = regex.matcher(url);
        if (matcher.find())
            platform = matcher.group(1);

        return platform.toUpperCase();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver(){
        switch (platform){
            case "POSTGRESQL":
                return "org.postgresql.Driver";
            case "ORACLE":
                return "oracle.jdbc.OracleDriver";
            case "SQLSERVER":
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            default:
                throw new RuntimeException("Database currently not supported.");
        }
    }

    public ConfigurationStoreEnvironment.DatabaseType getDatabaseType(){
        switch (platform){
            case "POSTGRESQL":
                return ConfigurationStoreEnvironment.DatabaseType.POSTGRES;
            case "ORACLE":
                return ConfigurationStoreEnvironment.DatabaseType.ORACLE;
            case "SQLSERVER":
                return ConfigurationStoreEnvironment.DatabaseType.SQLSERVER;
            default:
                throw new RuntimeException("Database currently not supported.");
        }
    }
}
