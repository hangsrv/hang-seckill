package com.hang.seckill.service;

import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.pojo.param.LoginParam;
import com.hang.seckill.pojo.result.Result;

public interface UserService {
    Result<Users> login(LoginParam loginParam);
}
