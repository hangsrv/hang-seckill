package com.hang.seckill.controller;

import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.OrderInfo;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.redis.RedisService;
import com.hang.seckill.redis.UserKey;
import com.hang.seckill.service.SeckillGoodsService;
import com.hang.seckill.service.SeckillOrderService;
import com.hang.seckill.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/order")
public class SeckillOrderController {
    @Autowired
    RedisService redisService;
    @Autowired
    SeckillOrderService seckillOrderService;
    @Autowired
    SeckillGoodsService seckillGoodsService;

    @GetMapping("/detail/{orderId}")
    public String info(Model model,
                       @PathVariable("orderId") long orderId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        if (user == null) {
            return "login";
        }
        OrderInfo orderInfo = seckillOrderService.getOrderInfo(orderId);
        if (orderInfo == null) {
            return "error/404";
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsBo goods = seckillGoodsService.getseckillGoodsBoByGoodsId(goodsId);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        model.addAttribute("user", user);
        return "order_detail";
    }

    @GetMapping("/list/{userId}")
    public String list(Model model,
                       @PathVariable("userId") long userId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        if (user == null) {
            return "login";
        }
        List<OrderInfo> orderInfos = seckillOrderService.getOrderInfoList(userId);
        model.addAttribute("orders", orderInfos);
        model.addAttribute("user", user);
        return "order_list";
    }
}
