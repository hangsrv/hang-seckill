package com.hang.seckill.controller;

import com.hang.seckill.common.Const;
import com.hang.seckill.mq.MQSender;
import com.hang.seckill.mq.SeckillMessage;
import com.hang.seckill.pojo.bo.GoodsBo;
import com.hang.seckill.pojo.entity.SeckillOrder;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.pojo.result.CodeMsg;
import com.hang.seckill.pojo.result.Result;
import com.hang.seckill.service.SeckillGoodsService;
import com.hang.seckill.service.SeckillOrderService;
import com.hang.seckill.util.CookieUtil;
import com.hang.seckill.util.JSONUtil;
import com.hang.seckill.util.RedisUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private MQSender mqSender;

    // 本地缓存
    private final HashMap<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 数据预热
     */
    public void afterPropertiesSet() throws Exception {
        List<GoodsBo> goodsList = seckillGoodsService.getSeckillGoodsList();
        if (goodsList == null) {
            return;
        }
        for (GoodsBo goods : goodsList) {
            redisUtil.set(Const.GOODS_STOCK_PREFIX + goods.getId(), JSONUtil.obj2Str(goods.getStockCount()), Const.GOODS_LIST_EXPIRE);
            localOverMap.put(goods.getId(), false);
        }
    }

    @GetMapping("/{path}/seckill/{goodsId}")
    @ResponseBody
    public Result<Integer> path(Model model,
                                @PathVariable("goodsId") long goodsId,
                                @PathVariable("path") String path,
                                HttpServletRequest request) {

        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisUtil.get(Const.USER_INFO_PREFIX + loginToken, Users.class);
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
        long stock = redisUtil.decr(Const.GOODS_STOCK_PREFIX + goodsId);
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
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @GetMapping("/result/{goodsId}")
    @ResponseBody
    public Result<Long> miaoshaResult(@PathVariable("goodsId") long goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisUtil.get(Const.USER_INFO_PREFIX + loginToken, Users.class);
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
        Users user = redisUtil.get(Const.USER_INFO_PREFIX + loginToken, Users.class);
        if (user == null) {
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        String path = seckillOrderService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }
}
