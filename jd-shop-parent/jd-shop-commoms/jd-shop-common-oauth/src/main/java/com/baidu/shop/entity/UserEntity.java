package com.baidu.shop.entity;


import com.baidu.shop.group.MingruiOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/15 17:51
 */
@Data
@Table(name = "tb_user")
@Api(value = "用户信息")
public class UserEntity {

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

    private Date created;

    private String salt;
}
