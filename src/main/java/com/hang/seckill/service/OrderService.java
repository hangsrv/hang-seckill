package com.hang.seckill.service;

import com.hang.seckill.pojo.entity.OrderInfo;

import java.util.List;

public interface OrderService {

    long addOrder(OrderInfo orderInfo);

    OrderInfo getOrderInfo(long orderId);

    List<OrderInfo> getOrderInfoList(long userId);
}
