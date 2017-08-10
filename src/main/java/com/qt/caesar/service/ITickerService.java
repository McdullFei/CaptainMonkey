package com.qt.caesar.service;

import com.qt.caesar.meta.Ticker;
import com.qt.caesar.meta.Depth;

/**
 * Created by renfei on 17/5/16.
 */
public interface ITickerService {
    Ticker getLastTicker(String coinType);


    /**
     * 获取市场深度
     * @param coinType
     * @return
     */
    Depth getLastDapth(String coinType);
}
