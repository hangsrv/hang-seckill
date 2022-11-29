package com.hang.seckill.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JSONUtil {

    public static <T> String obj2Str(T obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T str2obj(String objStr, Class<T> tClass) {
        return JSON.parseObject(objStr, tClass);
    }

    public static <T> List<T> str2ObjArr(String objStr, Class<T> tClass) {
        return JSON.parseArray(objStr, tClass);
    }

}
