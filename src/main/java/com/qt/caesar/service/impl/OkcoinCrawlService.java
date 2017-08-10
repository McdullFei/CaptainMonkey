package com.qt.caesar.service.impl;

import com.qt.caesar.service.ICrawlService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by renfei on 17/7/5.
 */
@Service
public class OkcoinCrawlService implements ICrawlService {

    private static final Logger logger = LoggerFactory.getLogger(OkcoinCrawlService.class);

    @Override
    public List<BigDecimal> getBuyOneSellOne() {

        try {
            Document doc = Jsoup.connect("https://www.okcoin.cn/market-btc.html")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                    .timeout(5000).get();


            Elements eLeft = doc.select(".real-left");
            Elements buyTrade = eLeft.select(".li-ct-transcation");
            Elements eRight = doc.select(".real-right");



        } catch (IOException e) {
            logger.error("e:", e);
        }

        return null;
    }
}
