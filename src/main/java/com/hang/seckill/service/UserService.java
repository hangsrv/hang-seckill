package com.hang.seckill.service;

import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.pojo.param.LoginParam;

public interface UserService {
    Users login(LoginParam loginParam);
}
