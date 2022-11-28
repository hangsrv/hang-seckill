DROP DATABASE IF EXISTS `hang-seckill`;
CREATE DATABASE `hang-seckill`;
USE `hang-seckill`;

# 商品表
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `goods_name`   varchar(16)    DEFAULT NULL COMMENT '商品名称',
    `goods_title`  varchar(64)    DEFAULT NULL COMMENT '商品标题',
    `goods_img`    varchar(64)    DEFAULT NULL COMMENT '商品图片',
    `goods_detail` longtext COMMENT '商品介绍详情',
    `goods_price`  decimal(10, 2) DEFAULT '0.00' COMMENT '商品单价',
    `goods_stock`  int(11)        DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
    `create_date`  datetime       DEFAULT NULL,
    `update_date`  datetime       DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;

LOCK TABLES `goods` WRITE;
INSERT INTO `goods`
VALUES (1, ' iPhone X', ' 当天发/12分期/送大礼 Apple/苹果 iPhone X移动联通4G手机中移动', '/img/iphone.jpg',
        ' 当天发/12分期/送大礼 Apple/苹果 iPhone X移动联通4G手机中移动', 7268.00, 1000, '2022-07-12 19:06:20', '2023-07-12 19:06:20'),
       (2, 'xiaomi 8', ' 小米8现货【送小米耳机】Xiaomi/小米 小米8手机8plus中移动8se', '/img/xiaomi.jpg',
        ' 小米8现货【送小米耳机】Xiaomi/小米 小米8手机8plus中移动8se', 2799.00, 1000, '2022-07-12 19:06:20', '2023-07-12 19:06:20'),
       (3, '荣耀 10', ' 12期分期/honor/荣耀10手机中移动官方旗舰店正品荣耀10手机playv10 plαy', '/img/rongyao.jpg',
        ' 12期分期/honor/荣耀10手机中移动官方旗舰店正品荣耀10手机playv10 plαy', 2699.00, 1000, '2022-07-12 19:06:20', '2023-07-12 22:32:20'),
       (4, 'oppo find x', ' OPPO R15 oppor15手机全新机限量超薄梦境r15梦镜版r11s find x', '/img/oppo.jpg',
        ' OPPO R15 oppor15手机全新机限量超薄梦境r15梦镜版r11s find x', 4999.00, 1000, '2022-07-12 19:06:20', '2023-07-12 19:06:20');
UNLOCK TABLES;

# 订单信息表
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`       bigint(20)     DEFAULT NULL COMMENT '用户id',
    `goods_id`      bigint(20)     DEFAULT NULL COMMENT '商品id',
    `addr_id`       bigint(20)     DEFAULT NULL COMMENT '收货地址id',
    `goods_name`    varchar(16)    DEFAULT NULL COMMENT '冗余过来的商品名称',
    `goods_count`   int(11)        DEFAULT NULL COMMENT '商品数量',
    `goods_price`   decimal(10, 2) DEFAULT NULL COMMENT '商品价格',
    `order_channel` int(2)         DEFAULT '0' COMMENT '支付通道：1 PC、2 Android、3 ios',
    `status`        int(2)         DEFAULT NULL COMMENT '订单状态：0 未支付，1已支付，2 已发货，3 已收货，4 已退款，‘5 已完成',
    `create_date`   datetime       DEFAULT NULL,
    `pay_date`      datetime       DEFAULT NULL COMMENT '支付时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 43
  DEFAULT CHARSET = utf8;

# 秒杀商品表
DROP TABLE IF EXISTS `seckill_goods`;
CREATE TABLE `seckill_goods`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `goods_id`     bigint(20)     DEFAULT NULL COMMENT '商品id',
    `seckil_price` decimal(10, 2) DEFAULT NULL COMMENT '秒杀价',
    `stock_count`  int(11)        DEFAULT NULL COMMENT '秒杀数量',
    `start_date`   datetime       DEFAULT NULL,
    `end_date`     datetime       DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;

LOCK TABLES `seckill_goods` WRITE;
INSERT INTO `seckill_goods`
VALUES (1, 1, 6888.00, 100, '2022-07-12 19:06:20', '2023-08-15 19:06:20'),
       (2, 2, 2699.00, 100, '2022-07-17 22:32:20', '2023-08-15 19:06:20'),
       (3, 3, 2599.00, 100, '2022-07-14 00:59:20', '2023-08-15 19:06:20'),
       (4, 4, 4999.00, 100, '2022-07-17 09:06:20', '2023-08-15 19:06:20');
UNLOCK TABLES;

# 秒杀订单表
DROP TABLE IF EXISTS `seckill_order`;
CREATE TABLE `seckill_order`
(
    `id`       bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`  bigint(20) DEFAULT NULL,
    `order_id` bigint(20) DEFAULT NULL,
    `goods_id` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `u_userid_goodsid` (`user_id`, `goods_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 43
  DEFAULT CHARSET = utf8;

# 用户表
DROP TABLE IF EXISTS users;
CREATE TABLE `users`
(
    `id`              int(11) NOT NULL AUTO_INCREMENT,
    `user_name`       varchar(45) DEFAULT NULL,
    `phone`           varchar(11) DEFAULT NULL,
    `password`        varchar(65) DEFAULT NULL,
    `salt`            varchar(45) DEFAULT NULL,
    `head`            varchar(45) DEFAULT NULL,
    `login_count`     int(11)     DEFAULT NULL,
    `register_date`   datetime    DEFAULT NULL,
    `last_login_date` datetime    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1410085408
  DEFAULT CHARSET = utf8;