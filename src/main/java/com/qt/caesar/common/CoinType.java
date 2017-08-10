package com.qt.caesar.common;

/**
 * Created by renfei on 17/5/25.
 */
public enum CoinType {
    LTC_BTC("ltc_cny", "tLTCBTC"), ETC_BTC("etc_cny", "tETCBTC"), DASH_BTC("dash_cny","tDSHBTC"),
    ETH_BTC("eth_cny","tETHBTC"), ZEC_BTC("zec_cny","tZECBTC");

    private String chbtcCoin;//chbtc的货币类型
    private String bitfinexCoin;//bitfinex的货币类型

    CoinType(String chbtcCoin, String bitfinexCoin){
        this.chbtcCoin = chbtcCoin;
        this.bitfinexCoin = bitfinexCoin;
    }

    public String getChbtcCoin(){
        return this.chbtcCoin;
    }

    public String getBitfinexCoin(){
        return this.bitfinexCoin;
    }
}
