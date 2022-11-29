package com.hang.seckill.mapper;

import com.hang.seckill.pojo.entity.OrderInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface OrderInfoMapper {

    @Insert({
            "insert into order_info (user_id, ",
            "goods_id, addr_id, goods_name, ",
            "goods_count, goods_price, ",
            "order_channel, status, ",
            "create_date)",
            "values (#{userId,jdbcType=BIGINT}, ",
            "#{goodsId,jdbcType=BIGINT}, #{addrId,jdbcType=BIGINT}, #{goodsName,jdbcType=VARCHAR}, ",
            "#{goodsCount,jdbcType=INTEGER}, #{goodsPrice,jdbcType=DECIMAL}, ",
            "#{orderChannel,jdbcType=INTEGER}, #{status,jdbcType=INTEGER}, ",
            "#{createDate,jdbcType=TIMESTAMP})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(OrderInfo record);

    @Select({
            "select",
            "id, user_id, goods_id, addr_id, goods_name, goods_count, goods_price, order_channel, ",
            "status, create_date ",
            "from order_info",
            "where id = #{id,jdbcType=BIGINT}"
    })
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "user_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "goods_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "addr_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "goods_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "goods_count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "goods_price", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),
            @Arg(column = "order_channel", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "create_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
    })
    OrderInfo selectByPrimaryKey(Long id);

    @Select({
            "select",
            "id, user_id, goods_id, addr_id, goods_name, goods_count, goods_price, order_channel, ",
            "status, create_date",
            "from order_info",
            "where user_id = #{userId,jdbcType=BIGINT}"
    })
    @ConstructorArgs({
            @Arg(column = "id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
            @Arg(column = "user_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "goods_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "addr_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
            @Arg(column = "goods_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Arg(column = "goods_count", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "goods_price", javaType = BigDecimal.class, jdbcType = JdbcType.DECIMAL),
            @Arg(column = "order_channel", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Arg(column = "create_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
    })
    List<OrderInfo> getOrderInfoList(long userId);

}