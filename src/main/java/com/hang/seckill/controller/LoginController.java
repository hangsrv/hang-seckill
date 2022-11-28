package com.hang.seckill.controller;

import com.hang.seckill.common.Const;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.pojo.param.LoginParam;
import com.hang.seckill.pojo.result.Result;
import com.hang.seckill.redis.RedisService;
import com.hang.seckill.redis.UserKey;
import com.hang.seckill.service.UserService;
import com.hang.seckill.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    RedisService redisService;

    @Autowired
    UserService userService;

    /***
     * 去登陆页面
     */
    @GetMapping("toLogin")
    public String toLogin(HttpServletRequest request, HttpServletResponse response) {
        String loginToken = CookieUtil.readLoginToken(request);
        Users user = redisService.get(UserKey.getByName, loginToken, Users.class);
        if (user == null) {
            return "login";
        }
        return "forward:/goods/list";
    }

    /**
     * 登陆
     */
    @PostMapping("/login")
    @ResponseBody
    public Result<Users> doLogin(HttpServletResponse response, HttpSession session, @Valid LoginParam loginParam) {
        Result<Users> login = userService.login(loginParam);
        if (login.isSuccess()) {
            CookieUtil.writeLoginToken(response, session.getId());
            redisService.set(UserKey.getByName, session.getId(), login.getData(), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return login;
    }

    /***
     * 登出
     */
    @GetMapping("/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        redisService.del(UserKey.getByName, token);
        return "login";
    }
}
