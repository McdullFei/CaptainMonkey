package com.qt.caesar.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * guava cahce 池，用于本地缓存
 * 
 * @author renfei
 *
 */
public class GuavaCache {

	private static final Logger log = LoggerFactory.getLogger(GuavaCache.class);

	/**
	 * 创建缓存
	 * 
	 * @param maxSize
	 * @param expireAfterWrite 毫秒
	 * @return
	 */
	public static <K, V> Cache<K, V> callableCached(int maxSize, long expireAfterWrite) {
		Cache<K, V> cache = CacheBuilder.newBuilder().maximumSize(maxSize)
				.expireAfterWrite(expireAfterWrite, TimeUnit.MILLISECONDS).removalListener(new RemovalListener<K, V>() {
					public void onRemoval(RemovalNotification<K, V> rn) {
						log.debug("guava cache: " + rn.getKey() + " has bean removed!");
					}
				}).build();
		return cache;
	}
}
