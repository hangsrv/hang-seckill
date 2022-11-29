package com.hang.seckill.controller;

import com.hang.seckill.common.Const;
import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.service.SeckillGoodsService;
import com.hang.seckill.util.CookieUtil;
import com.hang.seckill.util.JSONUtil;
import com.hang.seckill.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    // 商品列表
    @GetMapping("/list")
    public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisUtil.get(Const.USER_INFO_PREFIX + loginToken, Users.class);
        if (user != null) {
            model.addAttribute("user", user);
        }
        String goodsListStr = redisUtil.get(Const.GOODS_LIST_PREFIX, String.class);
        List<GoodsBo> goodsBoList;
        if (!StringUtils.isEmpty(goodsListStr)) {
            goodsBoList = JSONUtil.str2ObjArr(goodsListStr, GoodsBo.class);
        } else {
            goodsBoList = seckillGoodsService.getSeckillGoodsList();
            redisUtil.set(Const.GOODS_LIST_PREFIX, JSONUtil.obj2Str(goodsBoList), Const.GOODS_LIST_EXPIRE);
        }
        model.addAttribute("goodsList", goodsBoList);
        return "goods_list";
    }

    // 商品详情
    @GetMapping("/detail/{goodsId}")
    public String detail(Model model,
                         @PathVariable("goodsId") long goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisUtil.get(Const.USER_INFO_PREFIX + loginToken, Users.class);
        if (user != null) {
            model.addAttribute("user", user);
        }
        GoodsBo goods = redisUtil.get(Const.GOODS_DETAIL_PREFIX + goodsId, GoodsBo.class);
        if (goods == null) {
            goods = seckillGoodsService.getSeckillGoodsBoByGoodsId(goodsId);
            if (goods != null) {
                redisUtil.set(Const.GOODS_DETAIL_PREFIX + goodsId, JSONUtil.obj2Str(goods), Const.GOODS_INFO_EXPIRE);
            } else {
                return "error/404";
            }
        }
        Integer stock = redisUtil.get(Const.GOODS_STOCK_PREFIX + goodsId, Integer.class);
        goods.setStockCount(stock);
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < startAt) {//秒杀还没开始，倒计时
            remainSeconds = (int) ((startAt - now) / 1000);
        } else if (now > endAt) {//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {//秒杀进行中
            miaoshaStatus = 1;
        }
        model.addAttribute("seckillStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}

