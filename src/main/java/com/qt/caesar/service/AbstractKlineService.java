package com.qt.caesar.service;

import com.qt.caesar.dto.KlineBaseModel;
import com.qt.caesar.meta.Ticker;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by renfei on 17/5/17.
 */
@MappedSuperclass
public abstract class AbstractKlineService implements IKlineService{

    private static final Logger logger = LoggerFactory.getLogger(AbstractKlineService.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Async
    @Override
    public void crawlKline() {

        //最新btc价格
        Ticker ticker = this.getTicker("tBTCUSD");


        logger.info("你猜我是那个线程?{}", this.getCurrencyType().toString());
        for (String s : this.getCurrencyType()) {

            String cacheKey = this.getQueryNextTimeCacheKey(s);

            long queryTime = NumberUtils.toLong(stringRedisTemplate.opsForValue().get(cacheKey), Long.MIN_VALUE);

            String url = this.getUrl(s, String.valueOf(queryTime));
            String jsonStr = this.getRequestResult(url);

            logger.info("url:{},返回报文:{}", url, jsonStr);

            if(!StringUtils.isEmpty(jsonStr)){



                List<KlineBaseModel> list = (List<KlineBaseModel>) this.parseKlineDataAndSaveBatch(s, jsonStr.toString(), ticker.getPrice());

                long nextTime = 0;
                if(!CollectionUtils.isEmpty(list)){

                    for (KlineBaseModel klineDto : list) {

                        if(klineDto.getPointTime() > nextTime){
                            nextTime = klineDto.getPointTime();
                        }
                    }
                    stringRedisTemplate.opsForValue().set(cacheKey, String.valueOf(nextTime));
                }

            }
        }
    }

    /**
     * 请求返回
     * @param url
     * @return
     */
    protected abstract String getRequestResult(String url);

    /**
     * 货币对应类型类型
     * @return
     */
    protected abstract List<String> getCurrencyType();

    /**
     * 查询时间戳
     * @return
     */
    protected abstract String getQueryNextTimeCacheKey(String currencyType);

    /**
     * 请求的url
     * @param param
     * @return
     */
    protected abstract String getUrl(String... param);

    /**
     * btc price 单位根据具体信息来
     * @return
     */
    protected abstract Ticker getTicker(String coinType);

    /**
     * 解析kline数据并保存
     * @param currencyType
     * @param resultJson
     * @param btcPrice
     * @return
     */
    protected abstract List<? extends KlineBaseModel> parseKlineDataAndSaveBatch(String currencyType, String resultJson, BigDecimal btcPrice);

}
