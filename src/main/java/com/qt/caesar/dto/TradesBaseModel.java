package com.qt.caesar.dto;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

/**
 * Created by renfei on 17/5/17.
 */
@MappedSuperclass
public class TradesBaseModel extends BaseModel {

    @Column(name="biz_id")
    private Long bizId;

    @Column(name="trades_time")
    private Long tradesTime;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="tid", unique = true)
    private Long tid;

    @Column(name="type")
    private String type;

    @Column(name="trade_type", nullable = false)
    private String tradeType = "";

    @Column(name="btc_price")
    private BigDecimal btcPrice;

    @Column(name="add_time")
    private Long addTime;

    @Column(name="coin_type")
    private String coinType;

//    @Column(insertable = false, updatable = false)
//    private Long orderColumn;

//    public Long getOrderColumn() {
//        return orderColumn;
//    }
//
//    public void setOrderColumn(Long orderColumn) {
//        this.orderColumn = orderColumn;
//    }

    @Column(name="buy")
    private BigDecimal buy;

    @Column(name="sell")
    private BigDecimal sell;

    @Column(name="sync_id")
    private long syncId = 0;

    public long getSyncId() {
        return syncId;
    }

    public void setSyncId(long syncId) {
        this.syncId = syncId;
    }

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal sell) {
        this.sell = sell;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public Long getTradesTime() {
        return tradesTime;
    }

    public void setTradesTime(Long tradesTime) {
        this.tradesTime = tradesTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public BigDecimal getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(BigDecimal btcPrice) {
        this.btcPrice = btcPrice;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }
}
