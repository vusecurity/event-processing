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

package com.vusecurity.eventprocessing.configuration;

import org.ehcache.config.builders.*;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.Properties;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public JCacheCacheManager cacheManager() {
        logger.info("Initializing JCacheCacheManager");

        CachingProvider provider = Caching.getCachingProvider();

        javax.cache.CacheManager cacheManager = provider.getCacheManager(
                provider.getDefaultURI(), provider.getDefaultClassLoader(), new Properties()
        );

        String cacheName = "rules";

        // Check if the cache already exists
        javax.cache.Cache<Object, Object> cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            logger.info("Cache '{}' does not exist. Creating new cache.", cacheName);


            org.ehcache.config.CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class,
                            ResourcePoolsBuilder
                                    .heap(10000)
                                    .offheap(10, MemoryUnit.MB)
                    )
                    .withExpiry(ExpiryPolicyBuilder.noExpiration())
                    .build();

            javax.cache.configuration.Configuration<Object, Object> configuration =
                    Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);

            cacheManager.createCache(cacheName, configuration);
            logger.info("Cache '{}' created successfully.", cacheName);
        } else {
            logger.warn("Cache '{}' already exists. Skipping creation.", cacheName);
        }

        return new JCacheCacheManager(cacheManager);
    }
}