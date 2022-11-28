package com.hang.seckill.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private Integer id;

    private String userName;

    private String phone;

    private String password;

    private String salt;

    private String head;

    private Integer loginCount;

    private Date registerDate;

    private Date lastLoginDate;

}