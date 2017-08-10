package com.qt.caesar.common;

import com.qt.caesar.config.HttpProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

/**
 * Created by renfei on 17/5/10.
 */
@Component
public class RestConfiguration {

    private HttpProperties httpProperties;

    @Autowired
    public RestConfiguration(HttpProperties httpProperties){

        this.httpProperties = httpProperties;
    }

    @Bean
    @ConditionalOnMissingBean({RestOperations.class, RestTemplate.class})
    public RestOperations restOperations() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(httpProperties.getReadTimeout());
        requestFactory.setConnectTimeout(httpProperties.getConnectTimeout());

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        // 使用 utf-8 编码集的 conver 替换默认的 conver（默认的 string conver 的编码集为 "ISO-8859-1"）
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }
        }
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return restTemplate;
    }
}
