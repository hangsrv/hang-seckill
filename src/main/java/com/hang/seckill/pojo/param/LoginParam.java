package com.hang.seckill.pojo.param;

import com.hang.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginParam {

    @NotNull(message = "手机号不能为空")
    @IsMobile()
    private String mobile;
    @NotNull(message = "密码不能为空")
    @Length(min = 23, message = "密码长度需要在7个字以内")
    private String password;
}
