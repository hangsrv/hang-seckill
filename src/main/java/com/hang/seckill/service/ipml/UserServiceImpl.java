package com.hang.seckill.service.ipml;

import com.hang.seckill.mapper.UsersMapper;
import com.hang.seckill.pojo.entity.Users;
import com.hang.seckill.pojo.param.LoginParam;
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
    public Users login(LoginParam loginParam) {

        Users user = userMapper.selectByPhone(loginParam.getMobile());
        if (user == null) {
            return null;
        }
        String dbPwd = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(loginParam.getPassword(), saltDB);
        if (!StringUtils.equals(dbPwd, calcPass)) {
            return null;
        }
        user.setPassword(StringUtils.EMPTY);
        return user;
    }
}
