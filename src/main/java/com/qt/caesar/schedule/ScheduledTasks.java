package com.qt.caesar.schedule;

import com.qt.caesar.service.IBrushAmountService;
import com.qt.caesar.service.IKlineService;
import com.qt.caesar.service.ITradesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by renfei on 17/5/10.
 */
@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Resource(name = "bitfinexKlineService")
    private IKlineService bitfinexKlineService;

    @Resource(name = "bitfinexTradesService")
    private ITradesService bitfinexTradesService;

    @Value("${bg.start}")
    private boolean bgStart;

    @Resource(name = "brushAmountService")
    private IBrushAmountService brushAmountService;

    @Value("${biglobal.host}")
    private String bitglobalUrl;

    @Resource(name = "tradesSyncService")
    private IBrushAmountService tradesSyncService;


    //@Scheduled(cron="${scheduled.kline}")
    public void chbtcKline() {
        if(bgStart){
            logger.info("sync Kline start");

            bitfinexKlineService.crawlKline();

            logger.info("sync Kline perEnd");

        }
    }

    @Scheduled(cron = "${scheduled.trades}")
    public void chbtctTrades(){
        if(bgStart){

            logger.info("sync trades start");

            bitfinexTradesService.crawlTrades();

            logger.info("sync trades perEnd");
        }
    }

//    @Scheduled(cron = "${scheduled.brush.amount}")
//    public void syncTrades(){
//        if(bgStart){
//
//            logger.info("sync syncTrades start");
//            String tranUrl = bitglobalUrl + "server/syncTrades";
//            tradesSyncService.exec(tranUrl, HttpMethod.POST);
//
//            logger.info("sync syncTrades perEnd");
//        }
//    }
}