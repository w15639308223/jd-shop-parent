package com.baidu.shop.web;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.business.UserOauthService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.UserEntity;
import com.baidu.shop.status.HttpStatus;
import com.baidu.shop.utils.CookieUtils;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utils.StringUtil;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/15 19:54
 */
@RestController
@Api(tags = "用户认证接口")
public class UserOauthController extends BaseApiService {

    @Autowired
    private UserOauthService userOauthService;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping(value = "oauth/login")
    @ApiOperation(value = "用户登录")
    public Result<JsonObject> login(@RequestBody UserEntity userEntity
            , HttpServletRequest request, HttpServletResponse response){
        //返回登录成功或者失败 通过账户去查询用户信息-->比对密码-->createToken
        String token = userOauthService.login(userEntity, jwtConfig);
        //判断token是否为null
        //true:用户名或密码错误
        if (StringUtil.isEmpty(token)){
            return this.setResultError(HttpStatus.ERROR,"用户名或密码错误");
        }
        //将token放到cookie中
        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),token,jwtConfig.getCookieMaxAge(),true);

        return this.setResultSuccess();
    }

    //@CookieValue(value = "MRSHOP_TOKEN") 从cookie中获取值value="cookie的属性名"
    @GetMapping(value = "oauth/verify")
    public Result<UserInfo> verifyUser(@CookieValue(value = "MRSHOP_TOKEN") String token
            , HttpServletRequest request , HttpServletResponse response){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            String s = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(), jwtConfig.getExpire());
            CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),s,jwtConfig.getCookieMaxAge(),true);

            return this.setResultSuccess(userInfo);
        } catch (Exception e) {//如果有异常 说明token有问题
            //e.printStackTrace();
            //应该新建http状态为用户验证失败,状态码为403
            return this.setResultError(HttpStatus.VERITY_ERROR,"用户失效");
        }
    }
}
