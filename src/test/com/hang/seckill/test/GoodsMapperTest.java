package com.hang.seckill.test;

import com.hang.seckill.mapper.GoodsMapper;
import com.hang.seckill.pojo.bo.GoodsBo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class GoodsMapperTest {
    @Autowired
    GoodsMapper goodsMapper;

    @Test
    public void getseckillGoodsBoByGoodsIdTest() {
        GoodsBo goodsBo = goodsMapper.getseckillGoodsBoByGoodsId(1L);
        log.info("{}", goodsBo);
    }

    @Test
    public void selectAllGoodesTest() {
        List<GoodsBo> goodsBoList = goodsMapper.selectAllGoodes();
        goodsBoList.forEach((goodsBo -> {
            log.info("{}", goodsBo);
        }));

    }
}
