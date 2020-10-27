package com.baidu.shop.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/21 15:28
 */
@Data
@Table(name = "tb_order_status")
public class OrderStatusEntity {

    @Id
    private Long orderId;//订单id

    private Integer status;//状态

    private Date createTime;//订单创建时间

    private Date paymentTime;//付款时间
}
