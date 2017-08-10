package com.qt.caesar.service;

/**
 *
 * 真正的交易处理
 *
 * Created by renfei on 17/7/6.
 */
public interface IRealTradeService {

    /**
     * 抢盘口
     * @param symbol  币种
     */
    void playTradeHand(String symbol);
}
