package com.baidu.shop.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.base.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName Category
 * @Description: TODO
 * @Author:王双全
 * @Date 2020/8/27
 * @Version V1.0
 **/
@Api(tags = "商品分类接口")
public interface CategoryService  {

    @ApiOperation(value = "通过查询商品分类")
    @GetMapping(value = "category/list")
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid);


    @ApiOperation(value = "删除商品分类")
    @DeleteMapping(value = "category/del")
    public  Result<JSONObject> delCategory(Integer id);


    //@RequestBody  接受json格式的参数 转为实体类
    @ApiOperation(value = "新增商品分类")
    @PostMapping(value = "category/add")
    public  Result<JSONObject> postCategory(@RequestBody CategoryEntity categoryEntity);

    @ApiOperation(value = "修改商品分类")
    @PutMapping(value = "category/modif")
    public  Result<JSONObject> putCategory(@RequestBody CategoryEntity categoryEntity);

    @ApiOperation(value = "根据品牌id查询分类信息")
    @GetMapping(value = "category/getByBrand")
    public  Result<List<CategoryEntity>> getByBrand(Integer brandId);

    @ApiOperation(value = "根据分类id集合查询分类信息")
    @GetMapping(value = "category/getCategoryIds")
    public  Result<List<CategoryEntity>> getCategoryByIdList(@RequestParam String cateIds);
}