package com.qt.caesar.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.qt.caesar.common.CoinType;
import com.qt.caesar.common.Constant;
import com.qt.caesar.common.IdGenerator;
import com.qt.caesar.dao.IBitfinexKlineDao;
import com.qt.caesar.dto.BitfinexKlineDto;
import com.qt.caesar.meta.Ticker;
import com.qt.caesar.service.AbstractKlineService;
import com.qt.caesar.service.ITickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by renfei on 17/5/17.
 */
@Service("bitfinexKlineService")
public class BitfinexKlineService extends AbstractKlineService {

    private static final Logger logger = LoggerFactory.getLogger(BitfinexKlineService.class);

    @Value("${bitfinex.kline.url}")
    private String klineUrl;

    @Resource(name = "bitfinexTickerService")
    private ITickerService tickerService;

    @Autowired
    private IBitfinexKlineDao bitfinexKlineDao;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    RestOperations restOperations;


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
    protected List<String> getCurrencyType() {
        List<String> result = Lists.newArrayList();

        for (CoinType coinType : CoinType.values()) {
            result.add(coinType.getBitfinexCoin());
        }

        return result;
    }

    @Override
    protected String getQueryNextTimeCacheKey(String currencyType) {
        return String.format(Constant.CacheKey.BITFINEX_KLINE_N_TIME, currencyType);
    }

    @Override
    protected String getUrl(String... param) {
        return String.format(klineUrl, param[0], param[1]);
    }

    @Override
    protected Ticker getTicker(String coinType) {
        return tickerService.getLastTicker(coinType);
    }

    @Override
    protected List<BitfinexKlineDto> parseKlineDataAndSaveBatch(String currencyType, String resultJson, BigDecimal btcPrice) {
        List<BitfinexKlineDto> result = Lists.newArrayList();

        long now = System.currentTimeMillis();

        if(!StringUtils.isEmpty(resultJson)){

            JSONArray jsonArray = JSONArray.parseArray(resultJson);
            if(jsonArray.size() > 0){
                for(int i =0;i<jsonArray.size();i++){
                    JSONArray data = jsonArray.getJSONArray(i);

                    /**
                     * MTS	int	millisecond time stamp
                     OPEN	float	First execution during the time frame
                     CLOSE	float	Last execution during the time frame
                     HIGH	float	Highest execution during the time frame
                     LOW	float	Lowest execution during the timeframe
                     VOLUME	float	Quantity of symbol traded within the timeframe
                     */
                    if(data.size() >= 6){
                        Long id = idGenerator.getIdByMongo(null);
                        BitfinexKlineDto dto = new BitfinexKlineDto();


                        long pointTime = data.getLong(0);
                        dto.setBizId(id);
                        dto.setPointTime(pointTime);
                        dto.setOpen(data.getBigDecimal(1));
                        dto.setHigh(data.getBigDecimal(3));
                        dto.setLow(data.getBigDecimal(4));
                        dto.setReceive(data.getBigDecimal(2));
                        dto.setTradingVolume(data.getBigDecimal(5));
                        dto.setType(Constant.KlineType._1MIN);
                        dto.setAddTime(now);
                        dto.setBtcPrice(btcPrice);
                        dto.setCoinType(currencyType);


                        result.add(dto);

                    }
                }


                this.saveBatch(result);

            }

        }

        return result;
    }

    private void saveBatch(List<BitfinexKlineDto> list) {
        for (BitfinexKlineDto klineDto : list) {
            try {

                bitfinexKlineDao.save(klineDto);
            }catch (Exception e){
                logger.info("唯一主键重复:{}" , klineDto.getBizId());
            }
        }
    }
}
