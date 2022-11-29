package com.hang.seckill.controller;

import com.hang.seckill.common.Const;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.pojo.param.LoginParam;
import com.hang.seckill.pojo.result.CodeMsg;
import com.hang.seckill.pojo.result.Result;
import com.hang.seckill.service.UserService;
import com.hang.seckill.util.CookieUtil;
import com.hang.seckill.util.JSONUtil;
import com.hang.seckill.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserService userService;

    /***
     * 去登陆页面
     */
    @PostMapping("/toLogin")
    public String toLogin(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request);
        Users user = redisUtil.get(Const.USER_INFO_PREFIX + token, Users.class);
        if (user == null) {
            return "login";
        }
        return "forward:/goods/list";
    }

    /**
     * 登陆
     */
    @PostMapping("/doLogin")
    @ResponseBody
    public Result<Users> doLogin(HttpServletResponse response, HttpSession session, LoginParam loginParam) {
        Users user = userService.login(loginParam);
        if (user == null) {
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        CookieUtil.writeLoginToken(response, session.getId());
        redisUtil.set(Const.USER_INFO_PREFIX + session.getId(), JSONUtil.obj2Str(user), Const.REDIS_SESSION_EXPIRE);
        return Result.success(user);
    }

    /***
     * 登出
     */
    @GetMapping("/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        redisUtil.del(Const.USER_INFO_PREFIX + token);
        return "login";
    }
}
