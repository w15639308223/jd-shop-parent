package com.baidu.dto;

import com.baidu.base.BaseDTO;
import com.baidu.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/3 17:46
 */
@ApiModel(value = "规格参数")
@Data
public class SpecParamDTO extends BaseDTO {

    @ApiModelProperty(value = "主键", example = "1")
    @NotNull(message = "主键不能为空", groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "分类id", example = "1")
    private Integer cid;

    @ApiModelProperty(value = "规格组id", example = "1")
    private Integer groupId;

    @ApiModelProperty(value = "规格参数名称")
    private String name;

    @ApiModelProperty(value = "是否是数字类型参数，1->true或0->false", example = "0")
    @NotNull(message = "是否是数字类型参数不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.update.class})
    private Boolean numeric;

    @ApiModelProperty(value = "数字类型参数的单位，非数字类型可以为空")
    private String unit;

    @ApiModelProperty(value = "是否是sku通用属性，1->true或0->false", example = "0")
    @NotNull(message = "是否是sku通用属性不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.update.class})
    private Boolean generic;

    @ApiModelProperty(value = "是否用于搜索过滤，1->true或0->false", example = "0")
    @NotNull(message = "是否用于搜索过滤不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.update.class})
    private Boolean  searching;

    @ApiModelProperty(value = "数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0")
    private String segments;
}
