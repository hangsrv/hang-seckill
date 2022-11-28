package com.hang.seckill.service;

import com.hang.seckill.pojo.bo.GoodsBo;

import java.util.List;

public interface SeckillGoodsService {

    List<GoodsBo> getSeckillGoodsList();

    GoodsBo getseckillGoodsBoByGoodsId(long goodsId);

    int reduceStock(long goodsId);
}
