package com.hang.seckill.common;

public class Const {

    // 缓存过期时间
    public interface RedisCacheExtime {
        // 会话
        int REDIS_SESSION_EXTIME = 60 * 30;//30分钟
        // 商品列表
        int GOODS_LIST = 60 * 30 * 24;//1分钟
        // 商品
        int GOODS_ID = 60;//1分钟
        // 秒杀路径
        int SECKILL_PATH = 60;//1分钟
        // 商品信息
        int GOODS_INFO = 60;//1分钟
    }
}
