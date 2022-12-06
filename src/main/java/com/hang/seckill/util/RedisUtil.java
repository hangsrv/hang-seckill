package com.hang.seckill.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取当个对象
     */
    public <T> T get(String key, Class<T> c) {
        String value = redisTemplate.opsForValue().get(key);
        return JSONUtil.str2obj(value, c);
    }

    /**
     * 设置对象
     */
    public Boolean set(String key, String value, int exTime) {
        try {
            if (exTime > 0) {
                redisTemplate.opsForValue().set(key, value, exTime, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean set(String key, String value) {
        return set(key, value, -1);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     */
    public <T> Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 增加值
     */
    public <T> Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 减少值
     */
    public Long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public String getAndDel(String key) {
        String value = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return value;
    }
}
