package com.hang.seckill.mq;

import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.SeckillOrder;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.service.OrderService;
import com.hang.seckill.service.SeckillGoodsService;
import com.hang.seckill.service.SeckillOrderService;
import com.hang.seckill.util.JSONUtil;
import com.hang.seckill.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SeckillGoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void MSReceive(String message) {
        log.info("MSReceive receive message:" + message);
        SeckillMessage mm = JSONUtil.str2obj(message, SeckillMessage.class);
        Users user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsBo goods = goodsService.getSeckillGoodsBoByGoodsId(goodsId);
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
    }
}
