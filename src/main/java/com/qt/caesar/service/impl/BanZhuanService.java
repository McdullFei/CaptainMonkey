package com.qt.caesar.service.impl;

import com.google.common.collect.Lists;
import com.qt.caesar.meta.Depth;
import com.qt.caesar.meta.Ticker;
import com.qt.caesar.meta.UserAccount;
import com.qt.caesar.service.IBanZhuanService;
import com.qt.caesar.service.ITradePlatformAccount;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by renfei on 17/7/7.
 */
public class BanZhuanService implements IBanZhuanService {

    private ITradePlatformAccount tradePlatformAccount;

    private BigDecimal depthLine = BigDecimal.valueOf(10);


    /**
     * 获取所有平台的最新交易价
     * @return
     */
    private List<Ticker> getAllLastTicker(){

        // TODO ~~~~~~~~~~~
        List<Ticker> list = Lists.newArrayList();

        return list;
    }

    private List<Depth> getAllLastDepth(){
        // TODO ~~~~~~~~~~~
        List<Depth> list = Lists.newArrayList();

        return list;
    }


    @Override
    public void doIt(String symbol) {
        //获取最新的交易价
        List<Ticker> tickerList = this.getAllLastTicker();

        //升序
        Collections.sort(tickerList, (o1, o2) -> {
            BigDecimal buy1 = o1.getBuyPrice();
            BigDecimal sell1 = o1.getSellPrice();

            BigDecimal buy2 = o2.getBuyPrice();
            BigDecimal sell2 = o2.getSellPrice();

            if(buy1.compareTo(buy2) > 0 && sell1.compareTo(sell2) > 0){

                return 1;
            }else if(buy1.compareTo(buy2) < 0 && sell1.compareTo(sell2) < 0){

                return -1;
            }else{
                BigDecimal price1 = o1.getPrice();
                BigDecimal price2 = o2.getPrice();

                if(price1.compareTo(price2) > 0){
                    return 1;
                }else if(price1.compareTo(price2) < 0){
                    return -1;
                }else{
                    return 0;
                }
            }
        });

        Ticker tickerHeight = tickerList.get(tickerList.size() - 1);//最高
        Ticker tickerLow = tickerList.get(0);//最低

        //通过市场深度查看
        List<Depth> list = this.getAllLastDepth();
        //TODO
        for (Depth depth : list) {

            //市场深度不够,放弃本次搬砖
            if(depth.getAsks().get(0).get(1).compareTo(depthLine) < 0){
                break;
            }
        }


        //比较最低最高平台的价格优势是否合理适合搬砖

        //获取平台账户余额
        UserAccount userAccount = tradePlatformAccount.get();
        //计算最低平台余额所能买的数量amount
        BigDecimal amount = BigDecimal.ZERO;








    }
}
