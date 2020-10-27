package com.baidu.shop.dto;

import com.baidu.shop.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/21 19:25
 */
@Data
@ApiModel(value = "订单数据传输")
public class OrderDTO {

    @ApiModelProperty(value = "收货地址id" ,example = "1")
    @NotNull(message = "收货地址不能为空", groups = {MingruiOperation.update.class})
    private Integer addrId;

    @ApiModelProperty(value = "支付方式" ,example = "1")
    @NotNull(message = "支付方式不能为空", groups = {MingruiOperation.update.class})
    private Integer payType;

    @ApiModelProperty(value = "购买商品ID集合")
    @NotEmpty(message = "购买商品不能为空", groups = {MingruiOperation.update.class})
    private String skuIds;
}
