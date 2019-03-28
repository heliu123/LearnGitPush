package com.example.springBoot.util.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
/**
 * 共享锁
 * @author zhufeng
 *
 */
public class JedisShareLockHandle {
    
    private static Logger logger = LogManager.getLogger(JedisShareLockHandle.class);
    

    
    public static final String UNLOCK_LUA;
 
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }
    /**
     * 加共享锁
     * @author zhufeng
     * @date 2018年10月30日
     * @param key
     * @param value
     * @param expire 有效期(毫秒)
     * @param retryTimes 重试次数
     * @param sleepMillis 重试间隔时长(毫秒)）
     * @return
     */
  /*  public static boolean lock(String key, String value, long expire, int retryTimes, long sleepMillis) {
        boolean result = setRedis(key, value, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while((!result) && retryTimes-- > 0){
            try {
                logger.debug("lock failed, retrying..." + retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = setRedis(key, value, expire);
        }
        return result;
    }*/
    /**
     * 执行加锁动作
     * @author zhufeng
     * @date 2018年10月30日
     * @param key
     * @param value
     * @param expire
     * @return
     */
   /* private static boolean setRedis(String key, String value, long expire) {
    	StringRedisTemplate  redisTemplate =  SpringUtil.getBean(JedisConnectionType.REDIS_NSQ_TOKEN, StringRedisTemplate.class);
        try {
            String result = redisTemplate.execute(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                    return commands.set(key, value, "NX", "PX", expire);
                }
            });
            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            logger.error("set redis occured an exception", e);
        }
        return false;
    }*/
    /**
     * 释放共享锁
     * @author zhufeng
     * @date 2018年10月30日
     * @param key
     * @param value
     * @return
     */
   /* public static boolean releaseLock(String key, String value) {
    	StringRedisTemplate  redisTemplate =  SpringUtil.getBean(JedisConnectionType.REDIS_NSQ_TOKEN, StringRedisTemplate.class);
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            List<String> keys = new ArrayList<String>();
            keys.add(key);
            List<String> args = new ArrayList<String>();
            args.add(value);
 
            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            
            Long result = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群模式
                    if (nativeConnection instanceof JedisCluster) {
                        return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                    }
 
                    // 单机模式
                    else if (nativeConnection instanceof Jedis) {
                        return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
                    }
                    return 0L;
                }
            });
            
            return result != null && result > 0;
        } catch (Exception e) {
            logger.error("release lock occured an exception", e);
        }
        return false;
    }*/
    
}
