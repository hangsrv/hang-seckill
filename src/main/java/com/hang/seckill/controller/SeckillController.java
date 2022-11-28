package com.hang.seckill.controller;

import com.hang.seckill.common.Const;
import com.hang.seckill.mq.MQSender;
import com.hang.seckill.mq.SeckillMessage;
import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.OrderInfo;
import com.hang.seckill.pojo.entity.SeckillOrder;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.pojo.result.CodeMsg;
import com.hang.seckill.pojo.result.Result;
import com.hang.seckill.redis.GoodsKey;
import com.hang.seckill.redis.RedisService;
import com.hang.seckill.redis.UserKey;
import com.hang.seckill.service.SeckillGoodsService;
import com.hang.seckill.service.SeckillOrderService;
import com.hang.seckill.util.CookieUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private MQSender mqSender;

    /**
     * 如果是集群情况下，需要达到一定量此缓存才能起到重大作用
     */
    private final HashMap<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 将库存初始化到本地缓存及redis缓存，原则上次块应该在创建秒杀活动时候触发的（为了演示，此项目没有创建活动逻辑，所有放在启动项目时候放进内存）
     */
    public void afterPropertiesSet() throws Exception {
        List<GoodsBo> goodsList = seckillGoodsService.getSeckillGoodsList();
        if (goodsList == null) {
            return;
        }
        for (GoodsBo goods : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, String.valueOf(goods.getId()), goods.getStockCount(), Const.RedisCacheExtime.GOODS_LIST);
            localOverMap.put(goods.getId(), false);
        }
    }

    /**
     * 发起秒杀
     */
    @GetMapping("/seckill")
    public String seckill(Model model,
                          @RequestParam("goodsId") long goodsId, HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        model.addAttribute("user", user);
        if (user == null) {
            return "login";
        }
        //判断库存
        GoodsBo goods = seckillGoodsService.getseckillGoodsBoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = seckillOrderService.insert(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }

    @GetMapping("/{path}/seckill/{goodsId}")
    @ResponseBody
    public Result<Integer> path(Model model,
                                @PathVariable("goodsId") long goodsId,
                                @PathVariable("path") String path,
                                HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        if (user == null) {
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        //验证path
        boolean check = seckillOrderService.checkPath(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, String.valueOf(goodsId));
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        SeckillMessage mm = new SeckillMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(mm);
        return Result.success(0);
    }

    /**
     * 客户端轮询查询是否下单成功
     * 1：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @GetMapping("/result/{goodsId}")
    @ResponseBody
    public Result<Long> miaoshaResult(@PathVariable("goodsId") long goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        if (user == null) {
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        long result = seckillOrderService.getSeckillResult((long) user.getId(), goodsId);
        return Result.success(result);
    }

    @GetMapping("/path/{goodsId}")
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, @PathVariable("goodsId") long goodsId) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        if (user == null) {
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        String path = seckillOrderService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }
}
