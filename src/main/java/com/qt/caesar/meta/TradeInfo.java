package com.qt.caesar.meta;

import java.math.BigDecimal;

/**
 * Created by renfei on 17/7/6.
 */
public class TradeInfo {
    private String orderId;

    private BigDecimal price;

    private BigDecimal amount;

    private String tradeType;// buy or sell
    private String symbol;// 币种

    public String getOrderId() {
        return orderId;
    }

    public TradeInfo setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public TradeInfo setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TradeInfo setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public TradeInfo setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public TradeInfo setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }
}
