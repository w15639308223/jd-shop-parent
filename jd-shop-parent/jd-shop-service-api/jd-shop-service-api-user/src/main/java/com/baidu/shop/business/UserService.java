package com.baidu.shop.business;


import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.group.MingruiOperation;
import com.google.gson.JsonObject;
import com.baidu.shop.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/13 14:56
 */
@Api(tags = "用户接口")
public interface UserService {

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "user/register")
    Result<JSONObject> register(@Validated({MingruiOperation.Add.class}) @RequestBody UserDTO userDTO);

    @ApiOperation(value = "给手机号发送验证码")
    @PostMapping(value = "user/sendValidCode")
    Result<JSONObject> sendValidCode(@RequestBody UserDTO userDTO);

    @ApiOperation(value = "校验用户名或者手机号唯一")
    @GetMapping(value = "user/check/{value}/{type}")
    Result<JsonObject> checkUserNameOrPhone(
            @PathVariable(value = "value") String value,
            @PathVariable(value = "type") Integer type);

    @ApiOperation(value = "校验用户输入的手机验证码")
    @GetMapping(value = "/user/checkCode")
    Result<JSONObject> checkCode(String phone,String code);
}
