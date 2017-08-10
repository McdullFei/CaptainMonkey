package com.qt.caesar.action;

import com.google.common.collect.Maps;
import com.qt.caesar.service.IBrushAmountService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by renfei on 17/6/5.
 */
@RestController
public class StartController {

    private static final Logger logger = LoggerFactory.getLogger(StartController.class);


    private static AtomicBoolean isRun = new AtomicBoolean(Boolean.TRUE);
    private static AtomicInteger num = new AtomicInteger(1);


    @Resource(name = "tradesSyncService")
    private IBrushAmountService tradesSyncService;

    @Value("${biglobal.host}")
    private String bitglobalUrl;

    private static Thread brushamountRunner;

    private static final int syncTime = 30;


//    @RequestMapping(value = "/start/brushamount", method= RequestMethod.GET)
    public String start() {

        if(brushamountRunner == null || !brushamountRunner.isAlive()){

            isRun.set(Boolean.TRUE);
            this.startThread();

        }

        logger.info("刷量程序启动,brushamountRunner:"+brushamountRunner.isAlive()+";程序执行频率"+syncTime+"s一次");

        return "刷量程序启动,brushamountRunner:"+brushamountRunner.isAlive()+";程序执行频率"+syncTime+"s一次";

    }

//    @RequestMapping(value = "/stop/brushamount", method= RequestMethod.GET)
    public String stop() {


        isRun.set(Boolean.FALSE);

        int nnn = num.get();

        num.set(1);


        logger.error("刷量程序暂停,已经完成数量次数:{},下次执行将从1开始计数", nnn);


        return "刷量程序暂停,已经完成数量次数:"+nnn+",下次执行将从1开始计数";

    }

//    @RequestMapping(value = "/test/brushamount", method= RequestMethod.GET)
    public String testThread() {



        return "brushamountRunner is alive:" + brushamountRunner.isAlive() + ";刷量次数:"+num.get()+";isRun :"+isRun.get();

    }



    @RequestMapping(value = "/once/brushamount", method= RequestMethod.GET)
    public String startOnce() {

        String tranUrl = bitglobalUrl + "server/syncTrades";

        Map<String, Long> r = tradesSyncService.exec(tranUrl, HttpMethod.POST);

        return "刷量程序启动,同步数据:"+r.toString();

    }




    private void startThread(){
        brushamountRunner = new Thread(() -> {

            while(isRun.get()){
                logger.info("刷量程序启动,完成第{}次刷量", num.getAndIncrement());

                try {

                    String tranUrl = bitglobalUrl + "server/syncTrades";

                    tradesSyncService.exec(tranUrl, HttpMethod.POST);


                    Thread.sleep(DateUtils.MILLIS_PER_SECOND * syncTime);


                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("sleep error.", e);
                }

            }

        });

        brushamountRunner.start();
    }

}
