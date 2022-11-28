package com.hang.seckill.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
@Slf4j
public class RedisClusterTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test01() {
        String k1 = "k1";
        String v1 = "v1";
        redisTemplate.opsForValue().set(k1, v1);
        String v = redisTemplate.opsForValue().get(k1);
        log.info("{}", v);
        Boolean delete = redisTemplate.delete(k1);
        log.info("{}", delete);
    }
}
