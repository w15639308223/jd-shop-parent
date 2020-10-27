package com.baidu.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/22 15:28
 */
@Data
@ApiModel(value = "支付数据传输")
public class PayInfoDTO {

    @ApiModelProperty(value = "订单编号",example = "1")
    @NotNull(message = "订单编号不能为空")
    private Long orderId;

    @ApiModelProperty(value = "总金额,实际金额(元$)")
    @NotEmpty(message = "总金额")
    private String totalAmount;

    @ApiModelProperty(value = "订单名称")
    @NotEmpty(message = "订单名称不能为空")
    private String orderName;

    @ApiModelProperty(value = "订单描述")
    @NotEmpty(message = "订单描述")
    private String description;
}
