package com.qt.caesar.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.qt.caesar.common.Constant;
import com.qt.caesar.meta.Depth;
import com.qt.caesar.meta.Ticker;
import com.qt.caesar.service.ITickerService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by renfei on 17/5/17.
 */
@Service("bitfinexTickerService")
public class BitfinexTickerService implements ITickerService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BitfinexTickerService.class);

    @Autowired
    private RestOperations restOperations;

    @Value("${bitfinex.ticker.url}")
    private String bitfinexTicker;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private static final long timeOut = DateUtils.MILLIS_PER_SECOND * 30;

    @Override
    public Ticker getLastTicker(String coinType) {
        BigDecimal btcPrice = BigDecimal.ZERO;
        BigDecimal buyPrice = BigDecimal.ZERO;
        BigDecimal sellPrice = BigDecimal.ZERO;



        //缓存中有则取缓存数据
        List<Object> ticker = stringRedisTemplate.opsForHash().values(String.format(Constant.CacheKey.BITFINEX_LAST_TICKER, coinType));
        if(!CollectionUtils.isEmpty(ticker)){


            return new Ticker().setPrice(new BigDecimal((String)ticker.get(0)))
                    .setBuyPrice(new BigDecimal((String)ticker.get(1)))
                    .setSellPrice(new BigDecimal((String)ticker.get(2)));
        }

        try {
            String tickerJsonStr = "";
            Object o = restOperations.getForObject(String.format(bitfinexTicker, coinType), Object.class);

            if(o!=null){
                tickerJsonStr = o.toString();
            }

            if(!StringUtils.isEmpty(tickerJsonStr)){
                JSONArray tickerJson = JSONArray.parseArray(tickerJsonStr);

                if(tickerJson.size() > 0){

                    /**
                     BID	float	Price of last highest bid    买1价格
                     BID_SIZE	float	Size of the last highest bid
                     ASK	float	Price of last lowest ask     卖1价格
                     ASK_SIZE	float	Size of the last lowest ask
                     DAILY_CHANGE	float	Amount that the last price has changed since yesterday
                     DAILY_CHANGE_PERC	float	Amount that the price has changed expressed in percentage terms
                     LAST_PRICE	float	Price of the last trade ---我们取的价格
                     VOLUME	float	Daily volume
                     HIGH	float	Daily high
                     LOW	float	Daily low
                     */
                    btcPrice = tickerJson.getBigDecimal(6);
                    buyPrice = tickerJson.getBigDecimal(0);
                    sellPrice = tickerJson.getBigDecimal(2);


                }

                Map<String, String> value = Maps.newLinkedHashMap();
                value.put("price", btcPrice.toPlainString());
                value.put("buyPrice", buyPrice.toPlainString());
                value.put("sellPrice", sellPrice.toPlainString());

                stringRedisTemplate.opsForHash().putAll(String.format(Constant.CacheKey.BITFINEX_LAST_TICKER, coinType), value);
                stringRedisTemplate.expire(String.format(Constant.CacheKey.BITFINEX_LAST_TICKER, coinType), timeOut, TimeUnit.MILLISECONDS);
            }

        }catch (Exception e){
            logger.error("error:", e);

        }

        return new Ticker().setPrice(btcPrice).setBuyPrice(buyPrice).setSellPrice(sellPrice);
    }

    @Override
    public Depth getLastDapth(String coinType) {
        //TODO ~~~~~~~~~~~~~~~~~
        return null;
    }
}
