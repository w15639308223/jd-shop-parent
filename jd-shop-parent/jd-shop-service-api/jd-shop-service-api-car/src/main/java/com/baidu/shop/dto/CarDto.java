package com.baidu.shop.dto;

import com.baidu.shop.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/19 19:22
 */
@Data
@ApiModel(value = "购物车数据")
public class CarDto {

    @ApiModelProperty(value = "用户ID" ,example = "1")
    private Integer userId;

    @ApiModelProperty(value = "skuId",example = "1")
    @NotNull(message = "skuId不能为空",groups = {MingruiOperation.Add.class})
    private Long skuId;

    @ApiModelProperty(value = "商品标题")
    @NotNull(message = "商品标题不能为空",groups = {MingruiOperation.Add.class})
    private String title;

    @ApiModelProperty(value = "商品图片")
    @NotNull(message = "商品图片不能为空",groups = {MingruiOperation.Add.class})
    private String image;

    @ApiModelProperty(value = "商品价格",example = "1")
    @NotNull(message = "商品价格不能为空",groups = {MingruiOperation.Add.class})
    private Long price;

    @ApiModelProperty(value = "商品数量",example = "1")
    @NotNull(message = "商品数量不能为空",groups = {MingruiOperation.Add.class})
    private Integer num;

    @ApiModelProperty(value = "规格参数")
    @NotNull(message = "规格参不能为空",groups = {MingruiOperation.Add.class})
    private String ownSpec;


}
