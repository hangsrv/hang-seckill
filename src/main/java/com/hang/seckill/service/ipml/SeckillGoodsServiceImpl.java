package com.hang.seckill.service.ipml;

import com.hang.seckill.mapper.GoodsMapper;
import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("seckillGoodsService")
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public List<GoodsBo> getSeckillGoodsList() {
        return goodsMapper.selectAllGoodes();
    }

    @Override
    public GoodsBo getSeckillGoodsBoByGoodsId(long goodsId) {
        return goodsMapper.getSeckillGoodsBoByGoodsId(goodsId);
    }

    @Override
    public int reduceStock(long goodsId) {
        return goodsMapper.reduceStock(goodsId);
    }
}
