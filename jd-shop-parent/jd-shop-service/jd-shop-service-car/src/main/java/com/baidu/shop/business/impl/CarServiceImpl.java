package com.baidu.shop.business.impl;


import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.config.JwtConfig;
import com.baidu.shop.constant.MrShopConstant;
import com.baidu.shop.dto.CarDto;
import com.baidu.shop.dto.UserInfo;
import com.baidu.shop.entity.SkuEntity;
import com.baidu.shop.feigin.GoodsFeign;
import com.baidu.shop.redis.repository.RedisRepository;
import com.baidu.shop.business.CarService;
import com.baidu.shop.utils.JSONUtil;
import com.baidu.shop.utils.JwtUtils;
import com.baidu.shop.utils.ObjectUtil;
import com.baidu.shop.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/19 20:06
 */
@RestController
@Slf4j
public class CarServiceImpl extends BaseApiService implements CarService {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private GoodsFeign goodsFeign;

    @Override
    public Result<JSONObject> addCar(CarDto car, String token) {

        //System.out.println("============");

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //通过userid和skuid获取商品数据
            CarDto redisCar= redisRepository.getHash(MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId(), car.getSkuId() + "" , CarDto.class);

            CarDto saveCar = null;
            log.debug("通过key : {} ,skuid : {} 获取到的数据为 : {}",MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId(),car.getSkuId(),redisCar);
            //true: num += num
            //false:
            if (ObjectUtil.isNOtNull(redisCar)){
                redisCar.setNum(car.getNum() + redisCar.getNum());
                 saveCar = redisCar;
                log.debug("当前用户购物车中有将要新增的商品,重新设置 num:{}",redisCar.getNum());
            }else{//当前用户购物车没有新增的商品信息,
                Result<SkuEntity> skuResult = goodsFeign.getSkuBySkuId(car.getSkuId());

                if (skuResult.getCode() == 200){
                    SkuEntity skuEntity = skuResult.getData();
                    car.setTitle(skuEntity.getTitle());
                    car.setImage(StringUtil.isNotEmpty(skuEntity.getImages()) ?
                            skuEntity.getImages().split(",")[0] : "");
                    car.setPrice(Long.valueOf(skuEntity.getPrice()));
                    car.setOwnSpec(skuEntity.getOwnSpec());
                    car.setUserId(userInfo.getId());
                    saveCar = car;
                    log.debug("新增商品到购物车redis,KEY : {} , skuid : {} , car : {}",MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId(),car.getSkuId(), JSONUtil.toJsonString(car));
                }
            }
            redisRepository.setHash(MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId()
                    ,car.getSkuId() + "", JSONUtil.toJsonString(saveCar));
            log.debug("新增到redis数据成功");
        } catch (Exception e) {//进catch说明token有问题
            e.printStackTrace();
        }


        return this.setResultSuccess();
    }

    @Override
    public Result<JSONObject> mergeCar(String clientCarList, String token) {

        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(clientCarList);
        List<CarDto> carList = com.alibaba.fastjson.JSONObject.parseArray(jsonObject.get("clientCarList").toString(), CarDto.class);

        carList.stream().forEach(car -> {
            this.addCar(car,token);
        });

        return this.setResultSuccess();

    }

    @Override
    public Result<List<CarDto>> getUserGoodsCar(String token) {
        //通过用户id获取购物车数据
        try {
            List<CarDto> carDtos = new ArrayList<>();
            //获取当前登录的用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            Map<String,String> goodsCarMap = redisRepository.getHash(MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId());
            goodsCarMap.forEach((key,value) ->{
                CarDto carDto = JSONUtil.toBean(value, CarDto.class);
                carDtos.add(carDto);
            });
            return  this.setResultSuccess(carDtos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.setResultError("内部错误");
    }
    @Override
    public Result<JSONObject> carNumUpdate(Long skuId ,Integer type , String token) {

        //获取当前登录用户
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            CarDto car = redisRepository.getHash(MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId(), skuId + "", CarDto.class);

            if(car != null){
                if(type == 1){
                    car.setNum(car.getNum() + 1);
                }else{
                    car.setNum(car.getNum() - 1);
                }
                redisRepository.setHash(MrShopConstant.USER_GOODS_CAR_PRE + userInfo.getId(), car.getSkuId() + "", JSONUtil.toJsonString(car));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.setResultSuccess();
    }
}
