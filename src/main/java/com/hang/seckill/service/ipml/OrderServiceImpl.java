package com.hang.seckill.service.ipml;

import com.hang.seckill.mapper.OrderInfoMapper;
import com.hang.seckill.pojo.entity.OrderInfo;
import com.hang.seckill.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Override
    public void addOrder(OrderInfo orderInfo) {
        orderInfoMapper.insert(orderInfo);
    }

    @Override
    public OrderInfo getOrderInfo(long orderId) {
        return orderInfoMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public List<OrderInfo> getOrderInfoList(long userId) {
        return orderInfoMapper.getOrderInfoList(userId);
    }
}
