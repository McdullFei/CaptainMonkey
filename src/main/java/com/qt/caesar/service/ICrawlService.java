package com.qt.caesar.service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by renfei on 17/7/5.
 */
public interface ICrawlService {
    /**
     * 其他网站的buy one 和sell one
     * @return
     */
    List<BigDecimal> getBuyOneSellOne();
}
