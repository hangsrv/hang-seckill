package com.hang.seckill.mapper;

import com.hang.seckill.pojo.entity.Users;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

@Mapper
public interface UsersMapper {

    @Select({
            "select",
            "id, user_name, phone, password, salt, head, login_count, register_date, last_login_date",
            "from users",
            "where phone = #{phone,jdbcType=VARCHAR}"
    })
    @ConstructorArgs({
            @Arg(column = "id", javaType = Integer.class, jdbcType = JdbcType.INTEGER, id = true),
            @Arg(column = "user_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "phone", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "password", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "salt", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "head", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "login_count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "register_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
            @Arg(column = "last_login_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP)
    })
    Users selectByPhone(String mobile);

}