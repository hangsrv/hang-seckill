package com.hang.seckill.service.ipml;

import com.hang.seckill.mapper.UsersMapper;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.pojo.param.LoginParam;
import com.hang.seckill.pojo.result.CodeMsg;
import com.hang.seckill.pojo.result.Result;
import com.hang.seckill.service.UserService;
import com.hang.seckill.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UsersMapper userMapper;

    @Override
    public Result<Users> login(LoginParam loginParam) {

        Users user = userMapper.selectByPhone(loginParam.getMobile());
        if (user == null) {
            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPwd = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(loginParam.getPassword(), saltDB);
        if (!StringUtils.equals(dbPwd, calcPass)) {
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.success(user);
    }
}
