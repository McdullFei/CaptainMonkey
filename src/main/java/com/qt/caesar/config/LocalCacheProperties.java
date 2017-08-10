package com.qt.caesar.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by renfei on 17/5/10.
 */
@Component
@ConfigurationProperties(prefix = "local.cache")
public class LocalCacheProperties {
    private String param;

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public List<CacheBean> buildCacheInfoList() {
        if(!StringUtils.isEmpty(param)){
            List<CacheBean> result = Lists.newArrayList();

            JSONArray array = JSON.parseArray(param);
            if(array.size() > 0){
                for(int i=0;i<array.size();i++){
                    JSONObject json = array.getJSONObject(i);
                    String key = json.getString("key");
                    Long maxSize = json.getLong("max_size");
                    Long expireAfterWrite = json.getLong("expire_after_write");

                    result.add(new CacheBean().setKey(key).setMaxSize(maxSize).setExpireAfterWrite(expireAfterWrite));
                }
            }

            return result;
        }else{
            return null;
        }
    }

    public class CacheBean{
        private String key;
        private Long maxSize;
        private Long expireAfterWrite;

        public String getKey() {
            return key;
        }

        public CacheBean setKey(String key) {
            this.key = key;
            return this;
        }

        public Long getMaxSize() {
            return maxSize;
        }

        public CacheBean setMaxSize(Long maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Long getExpireAfterWrite() {
            return expireAfterWrite;
        }

        public CacheBean setExpireAfterWrite(Long expireAfterWrite) {
            this.expireAfterWrite = expireAfterWrite;
            return this;
        }
    }
}
