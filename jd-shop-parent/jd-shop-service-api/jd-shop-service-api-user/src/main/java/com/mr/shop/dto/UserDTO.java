package com.mr.shop.dto;

import com.baidu.shop.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/13 14:46
 */
@Data
@ApiModel(value = "用户Dto")
public class UserDTO {

    @ApiModelProperty(value = "用户主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.update.class})
    private Integer id;

    @ApiModelProperty(value = "账户")
    @NotNull(message = "账户不能为空", groups = {MingruiOperation.Add.class})
    private String username;

    @ApiModelProperty(value = "密码")
    @NotNull(message = "密码不能为空", groups = {MingruiOperation.Add.class})
    private String password;

    @ApiModelProperty(value = "手机号")
    @NotNull(message = "手机号不能为空",groups = {MingruiOperation.Add.class})
    private String phone;

    //创建时间
    private Date  creted;

    //密码加密的salt值
    private String salt;
}
