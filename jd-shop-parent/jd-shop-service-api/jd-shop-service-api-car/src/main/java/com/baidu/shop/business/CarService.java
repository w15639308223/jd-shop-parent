package com.baidu.shop.business;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.CarDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/19 19:54
 */
@Api(tags = "购物车接口")
public interface CarService {

    @ApiOperation(value = "添加商品到购物车")
    @PostMapping(value = "car/addCar")
    Result<JSONObject> addCar(@RequestBody CarDto car, @CookieValue(value = "MRSHOP_TOKEN") String token);

    @ApiOperation(value = "合并购物车")
    @PostMapping(value = "car/mergeCar")
    Result<JSONObject> mergeCar(@RequestBody String clientCarList,@CookieValue(value = "MRSHOP_TOKEN") String token);

    @ApiOperation(value = "获取当前用户的购物车数据")
    @GetMapping(value = "car/getUserGoodsCar")
    Result<List<CarDto>> getUserGoodsCar(@CookieValue(value = "MRSHOP_TOKEN") String token);

    @ApiOperation(value = "修改商品在购物车中的数量")
    @GetMapping(value = "car/carNumUpdate")
    Result<JSONObject> carNumUpdate(Long skuId ,Integer type ,@CookieValue(value = "MRSHOP_TOKEN") String token);
}
