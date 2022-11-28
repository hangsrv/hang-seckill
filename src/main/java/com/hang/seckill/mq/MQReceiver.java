package com.hang.seckill.mq;

import com.hang.seckill.mapper.OrderInfoMapper;
import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.SeckillOrder;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.redis.RedisService;
import com.hang.seckill.service.OrderService;
import com.hang.seckill.service.SeckillGoodsService;
import com.hang.seckill.service.SeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillGoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillOrderService seckillOrderService;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void MSReceive(String message) {
        log.info("MSReceive receive message:" + message);
        SeckillMessage mm = RedisService.stringToBean(message, SeckillMessage.class);
        Users user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsBo goods = goodsService.getseckillGoodsBoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        seckillOrderService.insert(user, goods);
        orderInfoMapper.updateOrderStatus(goodsId, user.getId(), 1);
    }
}
