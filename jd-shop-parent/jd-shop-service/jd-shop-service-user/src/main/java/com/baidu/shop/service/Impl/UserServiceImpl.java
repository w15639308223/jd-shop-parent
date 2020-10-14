package com.baidu.shop.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.redis.repository.RedisRepository;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.constant.MrShopConstant;
import com.baidu.shop.constant.UserConstant;
import com.baidu.shop.mapper.UserMapper;
import com.baidu.shop.status.HttpStatus;
import com.baidu.shop.utils.BCryptUtil;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.google.gson.JsonObject;
import com.mr.shop.dto.UserDTO;
import com.mr.shop.entity.UserEntity;
import com.mr.shop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/13 15:19
 */
@RestController
@Slf4j
public class UserServiceImpl extends BaseApiService implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisRepository redisRepository;

    @Override
    public Result<JSONObject> register(UserDTO userDTO) {
        UserEntity userEntity = BaiduBeanUtil.copyProperties(userDTO, UserEntity.class);
        userEntity.setPassword(BCryptUtil.hashpw(userEntity.getPassword(),BCryptUtil.gensalt()));
        userEntity.setCreated(new Date());
        userMapper.insertSelective(userEntity);
        return this.setResultSuccess();
    }

    @Override
    public Result<JsonObject> checkUserNameOrPhone(String value, Integer type) {

        //测试键值对是否能存入Linux中的redistribution缓存  redisRepository.set("name","wsq111");
        Example example = new Example(UserEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if(type == UserConstant.USER_TYPE_USERNAME){

            criteria.andEqualTo("username",value);

        }else if(type == UserConstant.USER_TYPE_PHONE){
            criteria.andEqualTo("phone",value);
        }

        List<UserEntity> userEntities = userMapper.selectByExample(example);
        return this.setResultSuccess(userEntities);
    }


    @Override
    public Result<JSONObject> sendValidCode(UserDTO userDTO) {

        //生成随机6位验证码
        String code = (int)((Math.random() * 9 + 1) * 100000) + "";
        log.debug("发送短信验证码--->手机号:{} 验证码 :{}",userDTO.getPhone(),code);
        redisRepository.set(MrShopConstant.USER_PHONE_CODE_PRE + userDTO.getPhone(),code);
        redisRepository.expire(MrShopConstant.USER_PHONE_CODE_PRE + userDTO.getPhone(),60);
        //发送短信验证码
        //sendSpeak发送语音信息
        //SendCode发送短信信息
       // LuosimaoDuanxinUtil.SendCode(userDTO.getPhone(),code);
        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> checkCode(String phone, String code) {
        String s = redisRepository.get(MrShopConstant.USER_PHONE_CODE_PRE + phone);
        if(!code.equals(s)){
            return this.setResultError(HttpStatus.VALID_CODE_ERROR,"验证码输入错误");
        }

        return this.setResultSuccess();
    }
}
