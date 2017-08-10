package com.qt.caesar.common;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by renfei on 17/5/10.
 */
public abstract class Constant {
//    public static final List<String> CURRENCY_TYPE = Arrays.asList("ltc_cny","etc_cny");
//
//    public static final List<String> BITFINEX_CURRENCY_TYPE = Arrays.asList("tLTCBTC","tETCBTC");

    public interface KlineType{
        String _1MIN = "1min";
        String _5MIN = "5min";
        String _15MIN = "15min";
        String _30MIN = "30min";
        String _1DAY = "1day";
        String _7DAY = "7day";
    }

    /**
     * 缓存key
     */
    public interface CacheKey{


        String BITFINEX_LAST_TICKER = "bitfinex.ticker.%s";
        String BITFINEX_KLINE_N_TIME = "bitfinex.kline.next.t.%s";
        String BITFINEX_TRADES_N_TID = "bitfinex.trades.next.t.%s";


        String BITGLOBAL_TRADES_N_TIME = "bitglobal.trades.next.t";//同步交易量的查询时间
    }

    /**
     * 交易类型
     *
     */
    public interface TradesType{
        String SELL = "sell";
        String BUY = "buy";
    }

    public static final BigDecimal ETC_TARGET_BRUSH_AMOUNT = BigDecimal.valueOf(10000); // TODO 刷量的目标交易量,后面移到表中可后台配置
    public static final BigDecimal LTC_TARGET_BRUSH_AMOUNT = BigDecimal.valueOf(10000); // TODO 刷量的目标交易量,后面移到表中可后台配置

}
