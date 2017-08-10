package com.qt.caesar.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.qt.caesar.common.CoinType;
import com.qt.caesar.common.Constant;
import com.qt.caesar.common.IdGenerator;
import com.qt.caesar.dao.IBitfinexTradesDao;
import com.qt.caesar.dto.BitfinexTradesDto;
import com.qt.caesar.dto.TradesBaseModel;
import com.qt.caesar.dto.TradesDto;
import com.qt.caesar.meta.Ticker;
import com.qt.caesar.service.AbstractTradesService;
import com.qt.caesar.service.ITickerService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by renfei on 17/5/17.
 */
@Service("bitfinexTradesService")
public class BitfinexTradesService extends AbstractTradesService {

    private static final Logger logger = LoggerFactory.getLogger(BitfinexTradesService.class);

    @Resource(name = "bitfinexTickerService")
    private ITickerService tickerService;

    @Value("${bitfinex.trades.url}")
    private String url;

    @Autowired
    private RestOperations restOperations;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private IBitfinexTradesDao bitfinexTradesDao;

    @Override
    protected Ticker getTicker(String coinType) {
        return tickerService.getLastTicker(coinType);
    }

    @Override
    protected List<String> getCurrencyType() {
        List<String> result = Lists.newArrayList();

        for (CoinType coinType : CoinType.values()) {
            result.add(coinType.getBitfinexCoin());
        }

        return result;
    }

    @Override
    protected String getNextQueryPCacheKey(String currencyType) {
        return String.format(Constant.CacheKey.BITFINEX_TRADES_N_TID, currencyType);
    }

    @Override
    protected String getUrl(String... param) {
        return String.format(url, param[0], param[1]);
    }

    @Override
    protected String getRequestResult(String url) {
        String result = "";
        Object o = restOperations.getForObject(url, Object.class);

        if(o!=null){
            result = o.toString();
        }
        return result;
    }

    @Override
    protected ImmutablePair<List<? extends TradesBaseModel>, Long> parseTradesDataAndSaveBatch(String currencyType, String resultJson, Ticker ticker) {
        long now = System.currentTimeMillis();
        List<TradesDto> result = Lists.newArrayList();
        long nextQId = 0;

        if(!StringUtils.isEmpty(resultJson)){
            JSONArray jsonArray = JSONArray.parseArray(resultJson);

            if(jsonArray.size() > 0){

                for(int i = 0;i < jsonArray.size(); i++){
                    long id = idGenerator.getIdByRedis(TradesDto.class.getSimpleName());


                    JSONArray json = jsonArray.getJSONArray(i);
                    long tradesTime = json.getLong(1);
                    BigDecimal amount = json.getBigDecimal(2);

                    BitfinexTradesDto dto = new BitfinexTradesDto();
                    dto.setAddTime(now);
                    dto.setAmount(amount);
                    dto.setBizId(id);
                    dto.setBtcPrice(ticker.getPrice());
                    dto.setCoinType(currencyType);
                    dto.setPrice(json.getBigDecimal(3));
                    dto.setTid(json.getLong(0));
                    dto.setTradesTime(tradesTime);
//                    dto.setTradeType(json.getString("trade_type"));



                    //获取买一卖一价格
                    Ticker cTicker = this.getTicker(currencyType);
                    dto.setBuy(cTicker.getBuyPrice());
                    dto.setSell(cTicker.getSellPrice());

                    if(BigDecimal.ZERO.compareTo(amount) > 0){//amount为负数则为卖单,为正则为买单

                        dto.setType(Constant.TradesType.SELL);
                    }else{
                        dto.setType(Constant.TradesType.BUY);
                    }

                    if(tradesTime > nextQId){
                        nextQId = tradesTime;
                    }

                    try {
                        bitfinexTradesDao.save(dto);

                    }catch (DataIntegrityViolationException e){
                        logger.info("唯一主键重复:{}" , dto.getTid());
                    }

                }
            }
        }

        return ImmutablePair.of(result, nextQId);
    }

    @Override
    protected String serviceName() {
        return this.getClass().getSimpleName();
    }
}
