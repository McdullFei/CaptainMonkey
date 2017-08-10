package com.qt.caesar.meta;

import java.math.BigDecimal;

/**
 * Created by renfei on 17/6/7.
 */
public class Ticker {
    private BigDecimal price;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;

    private String platform;

    public String getPlatform() {
        return platform;
    }

    public Ticker setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Ticker setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public Ticker setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public Ticker setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
        return this;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "price=" + price +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                ", platform='" + platform + '\'' +
                '}';
    }
}
