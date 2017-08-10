package com.qt.caesar.dto;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

/**
 * Created by renfei on 17/5/17.
 */
@MappedSuperclass
public class KlineBaseModel extends BaseModel{
    @Column(name="biz_id")
    private Long bizId;

    @Column(name="point_time")
    private Long pointTime;//点位时间
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal receive;

    @Column(name="trading_volume")
    private BigDecimal tradingVolume;

    private String type;

    @Column(name="add_time")
    private Long addTime;

    @Column(name="btc_price")
    private BigDecimal btcPrice;

    @Column(name="coin_type")
    private String coinType;

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getBtcPrice() {
        return btcPrice;
    }

    public void setBtcPrice(BigDecimal btcPrice) {
        this.btcPrice = btcPrice;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public Long getPointTime() {
        return pointTime;
    }

    public void setPointTime(Long pointTime) {
        this.pointTime = pointTime;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getReceive() {
        return receive;
    }

    public void setReceive(BigDecimal receive) {
        this.receive = receive;
    }

    public BigDecimal getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(BigDecimal tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }
}
