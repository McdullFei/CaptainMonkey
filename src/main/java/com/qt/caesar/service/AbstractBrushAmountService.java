package com.qt.caesar.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.io.CharStreams;
import com.qt.caesar.common.CoinType;
import com.qt.caesar.meta.BrushAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestOperations;

import javax.persistence.MappedSuperclass;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by renfei on 17/5/25.
 *
 * 刷量Service
 */
@MappedSuperclass
public abstract class AbstractBrushAmountService implements IBrushAmountService{

    private static final Logger logger = LoggerFactory.getLogger(AbstractBrushAmountService.class);

    @Autowired
    private RestOperations restOperations;

    /**
     * 主要处理类,经过复杂逻辑计算出目标交易量和价格
     *
     * @return
     */
    protected abstract BrushAmount buildBrushAmount(CoinType coinType);

    /**
     * 调用交易网站刷量
     */
    @Override
    public Map<String, Long> exec(String url, HttpMethod httpMethod){

        for (CoinType coinType : CoinType.values()) {// FIXME 可优化为并发

            BrushAmount amount = this.buildBrushAmount(coinType);

            if(amount != null){

                String requestUrl = url + "?price="+amount.getPrice() + "&amount="+amount.getTradingVolume()
                        + "&orderType=" + amount.getType() + "&coinType=" + amount.getCoinType();

                JSONObject json = restOperations.execute(requestUrl, httpMethod, null, clientHttpResponse -> {


                    if(clientHttpResponse.getStatusCode() == HttpStatus.OK){
                        InputStream is = clientHttpResponse.getBody();

                        String text = CharStreams.toString(new InputStreamReader(is, "UTF-8"));

                        return JSONObject.parseObject(text);

                    }else{
                        logger.error("平台接口服务不可用:{}", clientHttpResponse.getStatusText());
                    }

                    return null;
                }, amount);


                int code = json.getInteger("status");
                if(code == 0){//成功

                    logger.info("{}引量成功:交易量:{},交易价格:{},时间:{}",coinType.name(), amount.getTradingVolume(), amount.getPrice(), amount.getTime());
                }else{
                    logger.info("{}调用错误:{}", coinType.name(), json.toString());
                }
            }else{
                logger.info("没有可用的交易数据进行刷量");
            }

            // FIXME 可以优化这里每个币种可以单独起线程进行处理
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.info("每个币种稍微休息一段时间再执行");
            }

        }

        return null;

    }
}
