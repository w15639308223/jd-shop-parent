package com.baidu.shop.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/21 15:26
 */
@Data
@Table(name = "tb_order_detail")
public class OrderDetailEntity {

    @Id
    private Long id;//订单详情id

    private Long orderId;//订单id

    private Long skuId;//商品id

    private Integer num;//购买数量

    private String title;//商品标题

    private String ownSpec;//商品特有属性值

    private Long price;//商品价格

    private String image;//商品图片
}
