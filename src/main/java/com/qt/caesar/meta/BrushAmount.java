package com.qt.caesar.meta;

import java.math.BigDecimal;

/**
 * Created by renfei on 17/5/10.
 */
public class BrushAmount {
    private long time; //点位时间戳
    private BigDecimal price; //价格
    private String type; //kline类型
    private String coinType; //价格类型ltc_btc...
    private BigDecimal tradingVolume; //交易量

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(BigDecimal tradingVolume) {
        this.tradingVolume = tradingVolume;
    }
}
