package com.baidu.shop.service;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.BrandDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.group.MingruiOperation;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/8/31 15:17
 */
@Api(tags = "品牌接口")
public interface BrandService {

    @ApiOperation(value = "获取品牌信息")
    @GetMapping(value = "brand/getBrandInfo")
    public Result<PageInfo<BrandEntity>> getBrandInfo(@SpringQueryMap BrandDTO brandDTO);

    @ApiOperation(value = "增加品牌")
    @PostMapping(value = "brand/saveBrandInfo")
    public Result<JsonObject> saveBrandInfo(@Validated(MingruiOperation.Add.class) @RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "修改品牌信息" )
    @PutMapping(value = "brand/saveBrandInfo")
    public Result<JsonObject> editBrand(@Validated(MingruiOperation.update.class)@RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "删除品牌")
    @DeleteMapping(value = "brand/del")
    public  Result<JsonObject> delBrand(Integer id);

    @ApiOperation(value = "通过分类id查询品牌信息")
    @GetMapping(value = "brand/getBrandByCategory")
    public  Result<List<BrandEntity>> getBrandByCategory(Integer cid);

    @ApiOperation(value="通过品牌id集合获取品牌")
    @GetMapping(value = "brand/getBrandByIds")
    public   Result<List<BrandEntity>> getBrandByIds(@RequestParam String brandIds);
}
