package com.hang.seckill.common;

public class Const {

    // 会话
    public static int REDIS_SESSION_EXPIRE = 60 * 30;//30分钟
    // 商品列表
    public static int GOODS_LIST_EXPIRE = 60 * 30 * 24;//1分钟
    // 商品
    public static int GOODS_ID_EXPIRE = 60;//1分钟
    // 秒杀路径
    public static int SECKILL_PATH_EXPIRE = 60;//1分钟
    // 商品信息
    public static int GOODS_INFO_EXPIRE = 60;//1分钟

    public static String USER_INFO_PREFIX = "user_info_";
    public static String GOODS_LIST_PREFIX = "good_list";
    public static String GOODS_DETAIL_PREFIX = "good_detail_";
    public static String GOODS_STOCK_PREFIX = "good_stock_";
    public static String SECKILL_PATH_PREFIX = "seckill_path_";
    public static String SECKILL_IS_OVER_PREFIX = "seckill_is_over_";
}
