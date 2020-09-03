package com.baidu.service;

import com.baidu.base.Result;
import com.baidu.dto.BrandDTO;
import com.baidu.group.MingruiOperation;
import com.baidu.entity.BrandEntity;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO);

    @ApiOperation(value = "增加品牌")
    @PostMapping(value = "brand/saveBrandInfo")
    public Result<JsonObject> saveBrandInfo(@Validated(MingruiOperation.Add.class) @RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "修改品牌信息" )
    @PutMapping(value = "brand/saveBrandInfo")
    public Result<JsonObject> editBrand(@Validated(MingruiOperation.update.class)@RequestBody BrandDTO brandDTO);

    @ApiOperation(value = "删除品牌")
    @DeleteMapping(value = "brand/del")
    public  Result<JsonObject> delBrand(Integer id);
}
