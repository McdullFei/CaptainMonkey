package com.qt.caesar;

import com.google.common.collect.Lists;
import com.qt.caesar.meta.Ticker;
import com.qt.caesar.service.ICrawlService;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaesarApplicationTests {


	private static MessageDigest messageDigestForUserName = null;
	private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


	@Test
	public void contextLoads() throws NoSuchAlgorithmException {

		messageDigestForUserName = MessageDigest.getInstance("MD5");

		//通过登录的用户名做哈希分shard
		String userName = "renfei";
		byte[] btInput = userName.getBytes();
		messageDigestForUserName.update(btInput);
		messageDigestForUserName.update(btInput);
		byte[] md = messageDigestForUserName.digest();
		byte a = md[0];
		String x = "" + hexDigits[a >>> 4 & 0xf];
		x += hexDigits[a & 0xf];
		System.out.println("======="+Integer.parseInt(x, 16) % 128);


		//获取最新的交易价
		Ticker t1 = new Ticker().setBuyPrice(BigDecimal.valueOf(0)).setSellPrice(BigDecimal.valueOf(2))
				.setPrice(BigDecimal.valueOf(1));
		Ticker t2 = new Ticker().setBuyPrice(BigDecimal.valueOf(3)).setSellPrice(BigDecimal.valueOf(4))
				.setPrice(BigDecimal.valueOf(3.5));
		Ticker t3 = new Ticker().setBuyPrice(BigDecimal.valueOf(3.5)).setSellPrice(BigDecimal.valueOf(5))
				.setPrice(BigDecimal.valueOf(3.6));
		List<Ticker> tickerList = Lists.newArrayList();
		tickerList.add(t2);
		tickerList.add(t1);
		tickerList.add(t3);

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


		System.out.println(tickerList);

		//ICrawlService.getBuyOneSellOne();


		System.out.println(RandomUtils.nextInt(1));

		int td = RandomUtils.nextInt(10);
		double amountFin = (double) td / 10000d;
		System.out.println(amountFin);
	}

}
