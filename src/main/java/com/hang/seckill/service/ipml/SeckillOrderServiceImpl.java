package com.hang.seckill.service.ipml;

import com.hang.seckill.common.Const;
import com.hang.seckill.mapper.SeckillOrderMapper;
import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.OrderInfo;
import com.hang.seckill.pojo.entity.SeckillOrder;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.service.OrderService;
import com.hang.seckill.service.SeckillGoodsService;
import com.hang.seckill.service.SeckillOrderService;
import com.hang.seckill.util.MD5Util;
import com.hang.seckill.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service("seckillOrderService")
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrderService orderService;

    @Override
    public SeckillOrder getSeckillOrderByUserIdGoodsId(long userId, long goodsId) {
        return seckillOrderMapper.selectByUserIdAndGoodsId(userId, goodsId);
    }

    @Transactional
    @Override
    public OrderInfo insert(Users user, GoodsBo goods) {
        // 秒杀商品库存减一
        int success = seckillGoodsService.reduceStock(goods.getId());
        if (success == 1) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setCreateDate(new Date());
            orderInfo.setAddrId(0L);
            orderInfo.setGoodsCount(1);
            orderInfo.setGoodsId(goods.getId());
            orderInfo.setGoodsName(goods.getGoodsName());
            orderInfo.setGoodsPrice(goods.getSeckillPrice());
            orderInfo.setOrderChannel(1);
            orderInfo.setStatus(0);
            orderInfo.setUserId((long) user.getId());
            //添加信息进订单
            orderService.addOrder(orderInfo);
            log.info("orderId -->" + orderInfo.getId());
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setGoodsId(goods.getId());
            seckillOrder.setOrderId(orderInfo.getId());
            seckillOrder.setUserId((long) user.getId());
            //插入秒杀表
            seckillOrderMapper.addSeckillOrder(seckillOrder);
            return orderInfo;
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    @Override
    public OrderInfo getOrderInfo(long orderId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectByPrimaryKey(orderId);
        if (seckillOrder == null) {
            return null;
        }
        return orderService.getOrderInfo(seckillOrder.getOrderId());
    }

    @Override
    public List<OrderInfo> getOrderInfoList(long userId) {
        return orderService.getOrderInfoList(userId);
    }

    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {//秒杀成功
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public boolean checkPath(Users user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = redisUtil.getAndDel(Const.SECKILL_PATH_PREFIX + user.getId() + "_" + goodsId);
        return path.equals(pathOld);
    }

    public String createMiaoshaPath(Users user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String path = MD5Util.md5(UUID.randomUUID() + "123456");
        redisUtil.set(Const.SECKILL_PATH_PREFIX + user.getId() + "_" + goodsId, path, Const.SECKILL_PATH_EXPIRE);
        return path;
    }

    // 秒杀商品结束标记
    private void setGoodsOver(Long goodsId) {
        redisUtil.set(Const.SECKILL_IS_OVER_PREFIX + goodsId, "over", Const.GOODS_ID_EXPIRE);
    }

    // 查看秒杀商品是否已经结束
    private boolean getGoodsOver(long goodsId) {
        return redisUtil.exists(Const.SECKILL_IS_OVER_PREFIX + goodsId);
    }

}
