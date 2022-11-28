package com.hang.seckill.controller;

import com.hang.seckill.common.Const;
import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.redis.GoodsKey;
import com.hang.seckill.redis.RedisService;
import com.hang.seckill.redis.UserKey;
import com.hang.seckill.service.SeckillGoodsService;
import com.hang.seckill.util.CookieUtil;
import com.hang.seckill.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    /***
     * 商品列表
     */
    @GetMapping("/list")
    public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        if (user != null) {
            model.addAttribute("user", user);
        }
        String goodsListStr = redisService.get(GoodsKey.getGoodsList, "", String.class);
        List<GoodsBo> goodsBoList;
        if (!StringUtils.isEmpty(goodsListStr)) {
            goodsBoList = JsonUtil.str2ObjArr(goodsListStr, GoodsBo.class);
        } else {
            goodsBoList = seckillGoodsService.getSeckillGoodsList();
            redisService.set(GoodsKey.getGoodsList, "", JsonUtil.obj2Str(goodsBoList), Const.RedisCacheExtime.GOODS_LIST);
        }
        model.addAttribute("goodsList", goodsBoList);
        return "goods_list";
    }

    /***
     * 商品详情
     */
    @GetMapping("/detail/{goodsId}")
    public String detail(Model model,
                         @PathVariable("goodsId") long goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        if (user != null) {
            model.addAttribute("user", user);
        }
        GoodsBo goods = redisService.get(GoodsKey.getGoodsDetail, String.valueOf(goodsId), GoodsBo.class);
        if (goods == null) {
            goods = seckillGoodsService.getseckillGoodsBoByGoodsId(goodsId);
            if (goods != null) {
                redisService.set(GoodsKey.getGoodsDetail, String.valueOf(goodsId), goods, Const.RedisCacheExtime.GOODS_INFO);
            } else {
                return "error/404";
            }
        }
        Integer stock = redisService.get(GoodsKey.getSeckillGoodsStock, String.valueOf(goodsId), Integer.class);
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

