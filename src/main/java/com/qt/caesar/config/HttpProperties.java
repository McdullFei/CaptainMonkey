package com.qt.caesar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by renfei on 17/5/10.
 */
@Component
@ConfigurationProperties(prefix = "httpclient")
public class HttpProperties {
    private int readTimeout;

    private int connectTimeout;

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }
}
