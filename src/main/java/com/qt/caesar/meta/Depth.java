package com.qt.caesar.meta;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by renfei on 17/7/6.
 */
public class Depth {
    private List<List<BigDecimal>> asks;

    private List<List<BigDecimal>> bids;

    private String platform;

    public String getPlatform() {
        return platform;
    }

    public Depth setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public List<List<BigDecimal>> getAsks() {
        return asks;
    }

    public Depth setAsks(List<List<BigDecimal>> asks) {
        this.asks = asks;
        return this;
    }

    public List<List<BigDecimal>> getBids() {
        return bids;
    }

    public Depth setBids(List<List<BigDecimal>> bids) {
        this.bids = bids;
        return this;
    }
}
