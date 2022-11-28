package com.hang.seckill.util;

import com.hang.seckill.pojo.entity.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {

    private static List<Users> createUser(int count) throws Exception {
        List<Users> users = new ArrayList<>(count);
        //生成用户
        for (int i = 0; i < count; i++) {
            Users user = new Users();
            user.setLoginCount(1);
            user.setUserName("user" + i);
            user.setRegisterDate(new Date());
            user.setPhone((18077200000L + i) + "");
            user.setLastLoginDate(new Date());
            user.setSalt("9d5b364d");
            user.setHead("");
            user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
            users.add(user);
        }
        System.out.println("create user");
        //插入数据库
        Connection conn = DBUtil.getConn();
        String sql = "INSERT INTO `hang-seckill`.`users` (`user_name`, `phone`, `password`, `salt`, `head`, `login_count`," +
                " `register_date`, `last_login_date`)values(?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            Users user = users.get(i);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getSalt());
            pstmt.setString(5, user.getHead());
            pstmt.setInt(6, user.getLoginCount());
            pstmt.setTimestamp(7, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setTimestamp(8, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("insert to db");
        return users;
    }

    public static void main(String[] args) throws Exception {
        List<Users> user = createUser(1);
        System.out.println(user);
    }
}
