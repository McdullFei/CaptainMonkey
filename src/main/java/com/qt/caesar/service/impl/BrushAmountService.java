package com.qt.caesar.service.impl;

import com.google.common.collect.Lists;
import com.qt.caesar.common.CoinType;
import com.qt.caesar.common.Constant;
import com.qt.caesar.dao.IBitfinexKlineDao;
import com.qt.caesar.dao.IKlineDao;
import com.qt.caesar.dto.BitfinexKlineDto;
import com.qt.caesar.dto.KlineBaseModel;
import com.qt.caesar.dto.KlineDto;
import com.qt.caesar.meta.BrushAmount;
import com.qt.caesar.service.AbstractBrushAmountService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

/**
 * Created by renfei on 17/5/25.
 */
@Service("brushAmountService")
public class BrushAmountService extends AbstractBrushAmountService {


    @Autowired
    private IBitfinexKlineDao bitfinexKlineDao;

    @Autowired
    private IKlineDao klineDao;

    private static final BigDecimal BASE_PERCENT = BigDecimal.valueOf(0.3);


    @Override
    protected BrushAmount buildBrushAmount(CoinType coinType) {
        long now = System.currentTimeMillis();
        long pointTime = now - DateUtils.MILLIS_PER_HOUR;
        //获取当前时间后推5分钟的交易量
        List<BitfinexKlineDto> list = bitfinexKlineDao.findKlineDate(pointTime, coinType.getBitfinexCoin());

        List<KlineDto> list1 = klineDao.findKlineDate(pointTime, coinType.getChbtcCoin());


        List<KlineBaseModel> result = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(list)){
            result.addAll(list);
        }

        if(!CollectionUtils.isEmpty(list1)){
            result.addAll(list1);
        }

        if(!CollectionUtils.isEmpty(result)){

            Collections.shuffle(result);

            //随机取一条交易数据
            KlineBaseModel kline = result.get(0);
            BigDecimal price = kline.getReceive();
            BigDecimal tradingVolume = kline.getTradingVolume().multiply(BASE_PERCENT);


            BrushAmount ba = new BrushAmount();
            ba.setCoinType(coinType.name().toLowerCase());
            ba.setPrice(price);
            ba.setTradingVolume(tradingVolume);
            ba.setTime(now);
            ba.setType(Constant.KlineType._1MIN);

            return ba;

        }

        return null;
    }
}
