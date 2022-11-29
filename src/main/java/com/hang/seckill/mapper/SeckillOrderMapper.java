package com.hang.seckill.mapper;

import com.hang.seckill.pojo.entity.SeckillOrder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface SeckillOrderMapper {

    @Insert({
            "insert into seckill_order (user_id, ",
            "order_id, goods_id)",
            "values (#{userId,jdbcType=BIGINT}, ",
            "#{orderId,jdbcType=BIGINT}, #{goodsId,jdbcType=BIGINT})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addSeckillOrder(SeckillOrder record);

    @Select({
            "select",
            "id, user_id, order_id, goods_id",
            "from seckill_order",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "user_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "order_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "goods_id", javaType = Long.class, jdbcType = JdbcType.BIGINT)
    })
    SeckillOrder selectByPrimaryKey(Long id);

    @Select({
            "select",
            "id, user_id, order_id, goods_id",
            "from seckill_order",
            "where user_id = #{userId,jdbcType=BIGINT} and goods_id = #{goodsId}"
    })
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "user_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "order_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "goods_id", javaType = Long.class, jdbcType = JdbcType.BIGINT)
    })
    SeckillOrder selectByUserIdAndGoodsId(Long userId, Long goodsId);

}