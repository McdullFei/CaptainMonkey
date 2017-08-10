package com.qt.caesar.service;

import com.qt.caesar.meta.TradeInfo;

import javax.persistence.MappedSuperclass;
import java.util.List;

/**
 * Created by renfei on 17/7/6.
 */
@MappedSuperclass
public abstract class AbstractReaTradeService implements IRealTradeService {

    /**
     * 下单
     * @param symbol 币种
     * @param type 单类型（buy/sell）
     * @param price  下单价格
     * @param amount 交易数量
     */
    protected abstract boolean trade(String symbol, String type,
                               String price, String amount);


    /**
     * 撤单
     * @param symbol  币种
     * @param orderId  订单号
     * @return
     */
    protected abstract boolean cancelTrade(String symbol, String orderId);

    /**
     * 获取在挂的所有
     * @param symbol
     * @param status  成交状态  0：未成交 1：已完成
     * @return
     */
    protected abstract List<TradeInfo> getTrades(String symbol, int status);


    @Override
    public void playTradeHand(String symbol) {

    }
}
