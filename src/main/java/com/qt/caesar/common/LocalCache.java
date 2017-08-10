package com.qt.caesar.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.qt.caesar.config.LocalCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存 基于guava cache的cache manager
 * Created by renfei on 17/5/11.
 */
@Component
@EnableCaching
public class LocalCache{
    private LocalCacheProperties localCacheProperties;

    @Autowired
    public LocalCache(LocalCacheProperties localCacheProperties){

        this.localCacheProperties = localCacheProperties;
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<GuavaCache> cmList = Lists.newArrayList();

        List<LocalCacheProperties.CacheBean> beanList = localCacheProperties.buildCacheInfoList();
        if(!CollectionUtils.isEmpty(beanList)){
            for (LocalCacheProperties.CacheBean cacheBean : beanList) {
                CacheBuilder builder = CacheBuilder.newBuilder();
                if (cacheBean.getMaxSize() != -1L) {
                    builder.maximumSize(cacheBean.getMaxSize());
                }

                if(cacheBean.getExpireAfterWrite() != -1L){
                    builder.expireAfterWrite(cacheBean.getExpireAfterWrite(), TimeUnit.MINUTES);
                }
                GuavaCache cache = new GuavaCache(cacheBean.getKey(), builder.build());

                cmList.add(cache);
            }

        }


        cacheManager.setCaches(cmList);
        return cacheManager;
    }

}
