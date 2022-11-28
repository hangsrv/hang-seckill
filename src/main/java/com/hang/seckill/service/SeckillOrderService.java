package com.hang.seckill.service;

import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.OrderInfo;
import com.hang.seckill.pojo.entity.SeckillOrder;
import com.hang.seckill.pojo.entity.Users;

import java.util.List;

public interface SeckillOrderService {

    SeckillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId);

    OrderInfo insert(Users user, GoodsBo goodsBo);

    OrderInfo getOrderInfo(long orderId);

    List<OrderInfo> getOrderInfoList(long userId);

    long getSeckillResult(Long userId, long goodsId);

    boolean checkPath(Users user, long goodsId, String path);

    String createMiaoshaPath(Users user, long goodsId);

}
