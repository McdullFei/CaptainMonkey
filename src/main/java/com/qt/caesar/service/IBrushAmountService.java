package com.qt.caesar.service;

import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * Created by renfei on 17/5/25.
 */
public interface IBrushAmountService {
    Map<String, Long> exec(String url, HttpMethod httpMethod);
}
