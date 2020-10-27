package com.baidu.shop.business;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.SkuEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/7 15:14
 */
@Api(tags = "商品接口")
public interface GoodsService {

    @ApiOperation(value = "获取spu信息")
    @GetMapping(value = "goods/getSpuInfo")
    Result<List<SpuDTO>> getSpuInfo(@SpringQueryMap SpuDTO spuDTO);

    @ApiOperation(value = "新建商品")
    @PostMapping(value = "goods/saveGoods")
    Result<JsonObject> saveGoods(@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "根据spu获取详情信息")
    @GetMapping(value = "goods/getSpuDetailByIdSpu")
    Result<SpuDetailEntity> getSpuDetailByIdSpu(@RequestParam Integer spuId);

    @ApiOperation(value = "获取sku信息")
    @GetMapping(value = "goods/getSkuBySpu")
    Result<List<SkuDTO>> getSkuBySpu(@RequestParam Integer spuId);

    @ApiOperation(value = "修改商品")
    @PutMapping(value = "goods/saveGoods")
    Result<JsonObject>  editGoods(@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "修改商品上下架")
    @PutMapping(value = "goods/editSaleable")
    Result<JsonObject> editSaleable (@RequestBody SpuDTO spuDTO);

    @ApiOperation(value = "删除商品")
    @DeleteMapping(value = "goods/delGoods")
    Result<JsonObject> delGoods(Integer id);


    @ApiOperation(value = "通过skuid查询sku信息")
    @GetMapping(value = "goods/getSkuBySkuId")
    Result<SkuEntity> getSkuBySkuId(@RequestParam Long skuId);

}
