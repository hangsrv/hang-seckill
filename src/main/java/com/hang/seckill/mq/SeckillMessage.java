package com.hang.seckill.mq;


import com.hang.seckill.pojo.entity.Users;

public class SeckillMessage {
    private Users user;
    private long goodsId;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
