package com.qt.caesar.service.impl.okcoin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.qt.caesar.meta.Depth;
import com.qt.caesar.meta.Ticker;
import com.qt.caesar.service.ITickerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by renfei on 17/7/6.
 */
@Service
public class OkcoinTickerService implements ITickerService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OkcoinTickerService.class);

    @Autowired
    private RestOperations restOperations;

    @Value("${okcoin.host}")
    private String host;


    @Override
    public Ticker getLastTicker(String coinType) {

        Ticker ticker = null;

        String url = host + UriUtils.TICKER_URL;

        try {
            String tickerJsonStr = "";
            Object o = restOperations.getForObject(url + "symbol="+coinType, Object.class);

            if(o!=null){
                tickerJsonStr = o.toString();
            }

            if(!StringUtils.isEmpty(tickerJsonStr)){
                JSONObject tickerJson = JSONObject.parseObject(tickerJsonStr);

                if(tickerJson.containsKey("ticker")){
                    JSONObject t = tickerJson.getJSONObject("ticker");

                    ticker = new Ticker().setPrice(t.getBigDecimal("last")).setBuyPrice(t.getBigDecimal("buy"))
                            .setSellPrice(t.getBigDecimal("sell"));
                }

            }


        }catch (Exception e){
            logger.error("error:", e);

        }


        return ticker;
    }

    @Override
    public Depth getLastDapth(String coinType) {
        Depth depth = null;

        String url = host + UriUtils.DEPTH_URL;

        try {
            String depthJsonStr = "";
            Object o = restOperations.getForObject(url + "symbol="+coinType, Object.class);

            if(o!=null){
                depthJsonStr = o.toString();
            }

            if(!StringUtils.isEmpty(depthJsonStr)){
                JSONObject tickerJson = JSONObject.parseObject(depthJsonStr);

                if(tickerJson.containsKey("asks") && tickerJson.containsKey("bids")){
                    JSONArray t = tickerJson.getJSONArray("asks");
                    List<List<BigDecimal>> asks = Lists.newArrayList();
                    for(int i = 0;i < t.size(); i++){
                        JSONArray a = t.getJSONArray(i);
                        List<BigDecimal> a1 = Lists.newArrayList();

                        BigDecimal a11 = a.getBigDecimal(0);
                        BigDecimal a12 = a.getBigDecimal(1);

                        a1.add(a11);
                        a1.add(a12);


                        asks.add(a1);
                    }

                    JSONArray b = tickerJson.getJSONArray("bids");
                    List<List<BigDecimal>> bids = Lists.newArrayList();
                    for(int i = 0;i < b.size(); i++){
                        JSONArray a = b.getJSONArray(i);
                        List<BigDecimal> a1 = Lists.newArrayList();

                        BigDecimal a11 = a.getBigDecimal(0);
                        BigDecimal a12 = a.getBigDecimal(1);

                        a1.add(a11);
                        a1.add(a12);


                        bids.add(a1);
                    }

                    depth = new Depth().setAsks(asks).setBids(bids);
                }

            }


        }catch (Exception e){
            logger.error("error:", e);

        }


        return depth;
    }
}
