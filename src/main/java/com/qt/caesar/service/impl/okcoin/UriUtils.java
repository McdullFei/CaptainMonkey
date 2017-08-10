package com.qt.caesar.service.impl.okcoin;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by renfei on 17/7/6.
 */
public interface UriUtils {
    /**
     * 现货行情URL
     */
    String TICKER_URL = "/api/v1/ticker.do?";

    /**
     * 现货市场深度URL
     */
    String DEPTH_URL = "/api/v1/depth.do?";

    /**
     * 现货历史交易信息URL
     */
    String TRADES_URL = "/api/v1/trades.do?";

    /**
     * 现货获取用户信息URL
     */
    String USERINFO_URL = "/api/v1/userinfo.do?";

    /**
     * 现货 下单交易URL
     */
    String TRADE_URL = "/api/v1/trade.do?";

    /**
     * 现货 批量下单URL
     */
    String BATCH_TRADE_URL = "/api/v1/batch_trade.do";

    /**
     * 现货 撤销订单URL
     */
    String CANCEL_ORDER_URL = "/api/v1/cancel_order.do";

    /**
     * 现货 获取用户订单URL
     */
    String ORDER_INFO_URL = "/api/v1/order_info.do";

    /**
     * 现货 批量获取用户订单URL
     */
    String ORDERS_INFO_URL = "/api/v1/orders_info.do";

    /**
     * 现货 获取历史订单信息，只返回最近七天的信息URL
     */
    String ORDER_HISTORY_URL = "/api/v1/order_history.do";

    class Sign{

        /**
         * sign生成
         * @param paramMap  参数列表,随意排序
         * @return
         */
        public String X(Map<String, Object> paramMap, String secretKey){
            Assert.notNull(paramMap, "paramMap is not null.");

            // 对参数名进行字典排序
            String[] keyArray = paramMap.keySet().toArray(new String[paramMap.size()]);
            Arrays.sort(keyArray);

            StringBuilder sb = new StringBuilder();
            for (String s : keyArray) {
                sb.append(s).append("=").append(paramMap.get(s)).append("&");
            }

            sb.append("secret_key=").append(secretKey);




            return DigestUtils.md5Hex(sb.toString()).toUpperCase();
        }
    }
}
