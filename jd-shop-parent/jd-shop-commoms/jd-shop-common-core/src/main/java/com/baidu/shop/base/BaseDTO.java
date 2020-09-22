package com.baidu.shop.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/8/31 14:44
 */
@Data
@Api(value = "BaseDto用于数据传输,其他dto需要继承此类")
public class BaseDTO {

    @ApiModelProperty(value = "当前页",example = "1")
    private  Integer page;

    @ApiModelProperty(value = "每页多少条数据",example = "5")
    private Integer rows;

    @ApiModelProperty(value = "首字母")
    private  String sort;

    @ApiModelProperty(value = "是否排序")
    private Boolean desc;

    @ApiModelProperty(hidden = true)
    public String getOrderByClause(){

        if(!StringUtils.isEmpty(sort))return sort + " " + (desc?"desc":"");
        return null;
    }
}
