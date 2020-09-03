package com.baidu.dto;

import com.baidu.base.BaseDTO;
import com.baidu.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/8/31 15:01
 */
@ApiModel(value = "品牌")
@Data

public class BrandDTO extends BaseDTO {

        @ApiModelProperty(value = "品牌主键",example = "1")
        @NotNull(message = "主键不能为空",groups = MingruiOperation.update.class)
        private  Integer id;

        @ApiModelProperty(value = "品牌名称")
        @NotNull(message = "主键不能为空",groups = MingruiOperation.update.class)
        private  String name;

        @ApiModelProperty(value = "品牌照片")
        private  String image;

        @ApiModelProperty(value = "品牌首字母")
        private Character letter;

        @ApiModelProperty(value = "品牌分类信息")
        @NotEmpty(message = "品牌分类信息不能为空",groups = {MingruiOperation.Add.class})
        private String category;

}
