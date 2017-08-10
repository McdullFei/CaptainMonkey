package com.qt.caesar.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.qt.caesar.common.CoinType;
import com.qt.caesar.common.Constant;
import com.qt.caesar.dao.IBitfinexTradesDao;
import com.qt.caesar.dto.BitfinexTradesDto;
import com.qt.caesar.service.IBrushAmountService;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestOperations;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by renfei on 17/6/2.
 */
@Service
public class TradesSyncService implements IBrushAmountService {

    private static final Logger logger = LoggerFactory.getLogger(TradesSyncService.class);

    @Resource
    private IBitfinexTradesDao bitfinexTradesDao;

    @Autowired
    private RestOperations restOperations;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final BigDecimal p = BigDecimal.valueOf(0.8);

    @Value("${one.sync.test}")
    private boolean syncT;

    @Value("${biglobal.host}")
    private String bitglobalUrl;


    /**
     * 交易刷量：
     * LTC/BTC市场：24小时成交总量（BTC）为20个BTC，成交记录时间间隔为15min.
     * ETH/BTC市场：24小时成交总量（BTC）为20个BTC，成交记录时间间隔为15min.
     */
    private static double AVG_AMOUNT_PER_MIN = 0.14d;//24小时20个BTC 平均每分钟数量;该线程每分钟执行一次,只需要汇总一下每分钟amount的数就行

    private static final ConcurrentHashMap<String, Object> _D = new ConcurrentHashMap<>();//记录相同买一卖一的价格和时间

    interface DKey{
        String BUY_ONE = "buy";
        String SELL_ONE = "sell";
        String TIME = "time";
        String PRICE = "price";
    }



    @Override
    public Map<String, Long> exec(String url, HttpMethod httpMethod) {

        Map<String, Long> result = Maps.newLinkedHashMap();

        long now = System.currentTimeMillis();

        long defaultQueryTime = now - DateUtils.MILLIS_PER_MINUTE*10;

        long pointTime = NumberUtils.toLong(stringRedisTemplate.opsForValue().get(Constant.CacheKey.BITGLOBAL_TRADES_N_TIME), defaultQueryTime);

        logger.info("缓存中的查询时间:{}", new Date(pointTime));

        if(pointTime < defaultQueryTime){
            pointTime = defaultQueryTime;
        }

        logger.info("真实的查询时间:{}", new Date(pointTime));



        for (CoinType coinType : CoinType.values()) {

            long syncId = RandomUtils.nextInt(999999999);

            List<BitfinexTradesDto> list = bitfinexTradesDao.findTradesDate(pointTime, coinType.getBitfinexCoin());

            if (!CollectionUtils.isEmpty(list)) {

                //检查买一卖一有效性
                Boolean _PASS = Boolean.TRUE;
                JSONObject bsJson = restOperations.execute(bitglobalUrl + "server/buyOneSellOne?coinType=" + coinType.name().toLowerCase(), HttpMethod.POST, null, clientHttpResponse -> {


                    if (clientHttpResponse.getStatusCode() == HttpStatus.OK) {
                        InputStream is = clientHttpResponse.getBody();

                        String text = CharStreams.toString(new InputStreamReader(is, "UTF-8"));

                        return JSONObject.parseObject(text);

                    } else {
                        logger.error("平台接口服务不可用:{}", clientHttpResponse.getStatusText());
                    }

                    return null;
                });

                int c = bsJson.getInteger("status");
                if (c == 0) {//成功
                    JSONArray a = bsJson.getJSONArray("message");
                    BigDecimal buy = a.getBigDecimal(0);
                    BigDecimal sell = a.getBigDecimal(1);
                    if (_D.isEmpty()) {
                        _D.put(DKey.BUY_ONE, buy);
                        _D.put(DKey.SELL_ONE, sell);
                        _D.put(DKey.TIME, now);
                    } else {
                        BigDecimal lastBuyOne = (BigDecimal) _D.get(DKey.BUY_ONE);
                        BigDecimal lastSellOne = (BigDecimal) _D.get(DKey.SELL_ONE);
                        Long lastTime = (Long) _D.get(DKey.TIME);

                        if (lastBuyOne.compareTo(buy) == 0 && lastSellOne.compareTo(sell) == 0) {
                            if ((now - lastTime.longValue()) >= DateUtils.MILLIS_PER_MINUTE * 2) {//2分钟买一卖一不变则保持上次同步的价格进行刷量(约等于刷量停止)
                                _PASS = Boolean.FALSE;
                            } else {
                                // ~~~~维持lastTime不动
                            }
                        } else {
                            _D.put(DKey.BUY_ONE, buy);
                            _D.put(DKey.SELL_ONE, sell);
                            _D.put(DKey.TIME, now);
                        }
                    }


                } else {
                    return null;//买一卖一价格获取失败
                }


                /**========================================正式处理====================================**/
                if (_PASS) {

                    long t = Long.MIN_VALUE;
                    BigDecimal lastP = list.get(0).getPrice();

                    BigDecimal amoutTotal = BigDecimal.ZERO;
                    for (BitfinexTradesDto bitfinexTradesDto : list) {
                        BigDecimal priceFin = bitfinexTradesDto.getPrice();
                        BigDecimal amount = bitfinexTradesDto.getAmount().multiply(p);
                        double amountFin = Math.abs(amount.doubleValue());
                        String type = bitfinexTradesDto.getType();
                        BigDecimal buyOneFin = bitfinexTradesDto.getBuy();
                        BigDecimal sellOneFin = bitfinexTradesDto.getSell();


                        String requestUrl = url + "?price=" + priceFin + "&amount=" + amountFin + "&type=" + type + "&coinType=" + coinType.name().toLowerCase()
                                + "&buy=" + buyOneFin + "&sell=" + sellOneFin;

                        JSONObject json = restOperations.execute(requestUrl, httpMethod, null, clientHttpResponse -> {


                            if (clientHttpResponse.getStatusCode() == HttpStatus.OK) {
                                InputStream is = clientHttpResponse.getBody();

                                String text = CharStreams.toString(new InputStreamReader(is, "UTF-8"));

                                return JSONObject.parseObject(text);

                            } else {
                                logger.error("平台接口服务不可用:{}", clientHttpResponse.getStatusText());
                            }

                            return null;
                        }, amount);





                        int code = json.getInteger("status");
                        if (code == 0) {//成功

                            if (syncT) {//FIXME 这里只是给甜甜用来测试用的

                                bitfinexTradesDao.updateForSyncId(syncId, bitfinexTradesDto.getId());
                            }

                            logger.info("{}引量成功:交易量:{},交易价格:{},时间:{}", coinType.name(), amountFin, priceFin, bitfinexTradesDto.getTradesTime());


                            if (t < bitfinexTradesDto.getTradesTime()) {
                                t = bitfinexTradesDto.getTradesTime();
                            }

                            lastP = bitfinexTradesDto.getPrice();
                        } else {
                            logger.info("{}调用错误:{}", coinType.name(), json.toString());
                        }


                        //如果已经达到总量则停止刷量
                        amoutTotal = amoutTotal.add(amount);
                        if(amoutTotal.compareTo(new BigDecimal(AVG_AMOUNT_PER_MIN)) >=0){
                            break;
                        }

                        try {
                            //http://jira.mindasset.f3322.net:10080/browse/JYPT-613
                            int tt = RandomUtils.nextInt(6);
                            Thread.sleep(tt*100);
                        } catch (InterruptedException e) {
                            logger.info("e:", e);
                        }
                    }

                    result.put(coinType.name(), syncId);

                    stringRedisTemplate.opsForValue().set(Constant.CacheKey.BITGLOBAL_TRADES_N_TIME, String.valueOf(t));


                    _D.put(DKey.PRICE, lastP);
                } else {//买一卖一不动时的特殊处理
                    int i = list.size();

                    BigDecimal priceFin =(BigDecimal) _D.get(DKey.PRICE);
                    BigDecimal buyOneFin =(BigDecimal) _D.get(DKey.BUY_ONE);
                    BigDecimal sellOneFin = (BigDecimal)_D.get(DKey.SELL_ONE);

                    for (int i1 = 0; i1 < i; i1++) {

                        int td = RandomUtils.nextInt(10);
                        ++td;
                        double amountFin = (double) td / 10000d;
                        String type = "buy";
                        if(td % 2 == 0){
                            type = "sell";
                        }


                        String requestUrl = url + "?price=" + priceFin + "&amount=" + amountFin + "&type=" + type + "&coinType=" + coinType.name().toLowerCase()
                                + "&buy=" + buyOneFin + "&sell=" + sellOneFin;

                        restOperations.execute(requestUrl, httpMethod, null, clientHttpResponse -> {


                            if (clientHttpResponse.getStatusCode() == HttpStatus.OK) {
                                InputStream is = clientHttpResponse.getBody();

                                String text = CharStreams.toString(new InputStreamReader(is, "UTF-8"));

                                return JSONObject.parseObject(text);

                            } else {
                                logger.error("平台接口服务不可用:{}", clientHttpResponse.getStatusText());
                            }

                            return null;
                        });

                        try {
                            //http://jira.mindasset.f3322.net:10080/browse/JYPT-613
                            int tt = RandomUtils.nextInt(6);
                            Thread.sleep(tt*100);
                        } catch (InterruptedException e) {
                            logger.info("e:", e);
                        }
                    }
                }

            }
        }
        return result;

    }
}
