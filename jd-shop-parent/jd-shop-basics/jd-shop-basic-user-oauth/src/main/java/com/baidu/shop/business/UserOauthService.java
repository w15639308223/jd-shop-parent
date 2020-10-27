package com.baidu.shop.business;

import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.entity.UserEntity;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/15 19:17
 */
public interface UserOauthService {

    String login(UserEntity userEntity, JwtConfig jwtConfig);
}
