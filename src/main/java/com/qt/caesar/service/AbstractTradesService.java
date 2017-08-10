package com.qt.caesar.service;

import com.qt.caesar.dto.TradesBaseModel;
import com.qt.caesar.meta.Ticker;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

import javax.persistence.MappedSuperclass;
import java.util.List;

/**
 * Created by renfei on 17/5/17.
 */
@MappedSuperclass
public abstract class AbstractTradesService implements ITradesService{

    private static final Logger logger = LoggerFactory.getLogger(AbstractTradesService.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Async
    @Override
    public void crawlTrades() {

        logger.info("{}线程执行任务", this.serviceName());



        Ticker ticker = this.getTicker("tBTCUSD");

        for (String s : this.getCurrencyType()) {

            String cacheKey = this.getNextQueryPCacheKey(s);

            long nextTid = NumberUtils.toLong(stringRedisTemplate.opsForValue().get(cacheKey), 1);

            String url = this.getUrl(s, String.valueOf(nextTid));

            String jsonStr = this.getRequestResult(url);

            ImmutablePair<List<? extends TradesBaseModel>, Long> pair = this.parseTradesDataAndSaveBatch(s, jsonStr, ticker);

            List<TradesBaseModel> list = (List<TradesBaseModel>) pair.getLeft();

            if(!CollectionUtils.isEmpty(list)){

                stringRedisTemplate.opsForValue().set(cacheKey, String.valueOf(pair.getRight()));
            }

        }
    }

    protected abstract Ticker getTicker(String coinType);

    /**
     * 货币对应类型类型
     * @return
     */
    protected abstract List<String> getCurrencyType();

    /**
     * 查询的下一个时间点数据
     * @param currencyType
     * @return
     */
    protected abstract String getNextQueryPCacheKey(String currencyType);

    /**
     * 请求的url
     * @param param
     * @return
     */
    protected abstract String getUrl(String... param);

    /**
     * 请求返回
     * @param url
     * @return
     */
    protected abstract String getRequestResult(String url);

    /**
     * 解析交易数据并保存
     * @param currencyType
     * @param resultJson
     * @param ticker
     * @return L:结果集;R:下次查询的时间戳
     */
    protected abstract ImmutablePair<List<? extends TradesBaseModel>, Long> parseTradesDataAndSaveBatch(String currencyType, String resultJson, Ticker ticker);

    /**
     * 没有什么卵用
     * @return
     */
    protected abstract String serviceName();
}
