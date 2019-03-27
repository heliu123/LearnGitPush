package com.example.springBoot.util.lock;

import com.wtyt.tsr.util.common.StringUtil;
import com.wtyt.tsr.util.jedis.JedisConnectionType;
import com.wtyt.tsr.util.jedis.JedisHandle;
import com.wtyt.tsr.util.jedis.JedisKeysConstants;

/**
 * redis锁
 * @author zhufeng
 *
 */
public class JedisLockHandle {
	


	/**
	 * 加锁-无自动失效时间  按value逻辑失效
	 * @param key
	 * @param value 当前时间+超时时间
	 * @return 返回true表示加锁成功
	 */
	public static boolean lock(String key, String value) {
		key = JedisHandle.getKeyFromId(key, JedisKeysConstants.JEDIS_LOCK_KEY);
	    // 1.如果不存在key，则存入redis。并返回true
	    // 如果存在该key，则返回false
	    if(JedisHandle.setIfAbsent(JedisConnectionType.REDIS_NSQ_TOKEN, key, value)) {
	        return true;
	    }
	    String currentValue = JedisHandle.getValue(JedisConnectionType.REDIS_NSQ_TOKEN, key);
	    // 3.如果锁过期
	    if(!StringUtil.isEmptyStr(currentValue) && Long.valueOf(currentValue) < System.currentTimeMillis()) {
	        // 4.获取上一个锁的时间
	        // 将value设置到key， 并返回原来的value值
	        String oldValue = JedisHandle.getAndSet(JedisConnectionType.REDIS_NSQ_TOKEN, key, value);
	        // 5.此处的判断处于多线程
	        if(!StringUtil.isEmptyStr(oldValue) && oldValue.equals(currentValue)) {
	            return true;
	        }
	    }
	    return false;
	}

	
	/**
	 * 解锁-配套lock
	 * @author zhufeng
	 * @date 2018年9月12日
	 * @param key
	 * @param value
	 */
	public static void unlock(String key, String value) {
		key = JedisHandle.getKeyFromId(key, JedisKeysConstants.JEDIS_LOCK_KEY);
	    String currentValue = JedisHandle.getValue(JedisConnectionType.REDIS_NSQ_TOKEN, key);
	    if(!StringUtil.isEmptyStr(currentValue) && currentValue.equals(value)) {
	    	JedisHandle.delValue(JedisConnectionType.REDIS_NSQ_TOKEN, key);
	    }
	}
	
}
