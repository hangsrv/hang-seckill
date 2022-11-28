package com.hang.seckill.mapper;

import com.hang.seckill.pojo.bo.GoodsBo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface GoodsMapper {

    @Update({
            "update goods",
            "set goods_stock = goods_stock - 1",
            "where id = #{id,jdbcType=BIGINT}"
    })
    int updateStock(Long goodsId);

    @Select({
            "select",
            "g.id, g.goods_name, g.goods_title, g.goods_img, g.goods_price, g.goods_stock, g.create_date, g.update_date, g.goods_detail, ",
            "sg.seckil_price, sg.stock_count, sg.start_date, sg.end_date",
            "from goods g",
            "left join seckill_goods sg",
            "on g.id = sg.goods_id",
            "where g.id = #{goodsId,jdbcType=BIGINT}"
    })
    @ConstructorArgs({
            @Arg(column = "seckil_price", jdbcType = JdbcType.DECIMAL, javaType = BigDecimal.class),
            @Arg(column = "stock_count", jdbcType = JdbcType.INTEGER, javaType = Integer.class),
            @Arg(column = "start_date", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class),
            @Arg(column = "end_date", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class),
            @Arg(column = "g.id", jdbcType = JdbcType.BIGINT, javaType = Long.class),
            @Arg(column = "goods_name", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Arg(column = "goods_title", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Arg(column = "goods_img", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Arg(column = "goods_price", jdbcType = JdbcType.DECIMAL, javaType = BigDecimal.class),
            @Arg(column = "goods_stock", jdbcType = JdbcType.INTEGER, javaType = Integer.class),
            @Arg(column = "create_date", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class),
            @Arg(column = "update_date", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class),
            @Arg(column = "goods_detail", jdbcType = JdbcType.LONGVARCHAR, javaType = String.class)
    })
    GoodsBo getseckillGoodsBoByGoodsId(Long goodsId);

    @Select({
            "select",
            "g.id, g.goods_name, g.goods_title, g.goods_img, g.goods_price, g.goods_stock, g.create_date, g.update_date, g.goods_detail, ",
            "sg.seckil_price, sg.stock_count, sg.start_date, sg.end_date",
            "from goods g ",
            "left join seckill_goods sg ",
            "on g.id = sg.goods_id"
    })
    @ConstructorArgs({
            @Arg(column = "seckil_price", jdbcType = JdbcType.DECIMAL, javaType = BigDecimal.class),
            @Arg(column = "stock_count", jdbcType = JdbcType.INTEGER, javaType = Integer.class),
            @Arg(column = "start_date", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class),
            @Arg(column = "end_date", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class),
            @Arg(column = "id", jdbcType = JdbcType.BIGINT, javaType = Long.class),
            @Arg(column = "goods_name", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Arg(column = "goods_title", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Arg(column = "goods_img", jdbcType = JdbcType.VARCHAR, javaType = String.class),
            @Arg(column = "goods_price", jdbcType = JdbcType.DECIMAL, javaType = BigDecimal.class),
            @Arg(column = "goods_stock", jdbcType = JdbcType.INTEGER, javaType = Integer.class),
            @Arg(column = "create_date", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class),
            @Arg(column = "update_date", jdbcType = JdbcType.TIMESTAMP, javaType = Date.class),
            @Arg(column = "goods_detail", jdbcType = JdbcType.LONGVARCHAR, javaType = String.class)
    })
    List<GoodsBo> selectAllGoodes();
}