package com.baidu.shop.business.impl;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.business.UserOauthService;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.UserEntity;
import com.baidu.shop.mapper.UserOauthMapper;
import com.baidu.shop.utils.BCryptUtil;
import com.baidu.shop.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/15 20:41
 */
@Service
public class UserOauthServiceImpl extends BaseApiService implements UserOauthService {

    @Autowired
    private UserOauthMapper userOauthMapper;

    @Override
    public String login(UserEntity userEntity, JwtConfig jwtConfig) {
        String token = null;

        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("username",userEntity.getUsername());
        List<UserEntity> list = userOauthMapper.selectByExample(example);

        if (list.size() ==1){
            UserEntity entity = list.get(0);
            //比较密码   参数1:页面传过来的密码,参数2:输入库存放的密码
            if (BCryptUtil.checkpw(userEntity.getPassword(),entity.getPassword())) {
                //判断成功后创建token
                try {
                    //参数1:数据库存放的id 参数2:数据库存放的用户名 参数3:私钥 参数4:过期时间
                    token = JwtUtils.generateToken(new UserInfo(entity.getId(),entity.getUsername()),jwtConfig.getPrivateKey(),jwtConfig.getExpire());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return  token;
    }
}
