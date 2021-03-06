package com.baidu.shop.business;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/3 11:54
 */
@Api(tags = "规格接口")
public interface SpecGroupService {

     @ApiOperation(value = "通过分类条件查询规格组")
     @GetMapping(value = "specGroup/getSpecGroupInfo")
     Result<List<SpecGroupEntity>> getSpecGroupInfo(@SpringQueryMap SpecGroupDTO specGroupDTO);

     @ApiOperation(value = "新增规格组")
     @PostMapping(value = "specGroup/save")
     Result<JSONObject> saveSpecGroupInfo(@RequestBody SpecGroupDTO specGroupDTO);

     @ApiOperation(value = "修改规格组")
     @PutMapping(value = "specGroup/save")
     Result<JSONObject> editSpecGroupInfo(@RequestBody SpecGroupDTO specGroupDTO);

     @ApiOperation(value = "删除规格组")
     @DeleteMapping(value = "specGroup/del")
     Result<JSONObject> delSpecGroupInfo(Integer id);

     @ApiOperation(value = "查询规格参数")
     @GetMapping(value = "specparam/getSpecParamInfo")
     Result<List<SpecParamEntity>> getSpecParamInfo(@SpringQueryMap SpecParamDTO specParamDTO);

     @ApiOperation(value = "增加规格参数")
     @PostMapping(value = "specparam/saveSpecParam")
     Result<JSONObject> saveSpecParam(@RequestBody SpecParamDTO specParamDTO);

     @ApiOperation(value = "增加规格参数")
     @PutMapping(value = "specparam/saveSpecParam")
     Result<JSONObject> editSpecParam(@RequestBody SpecParamDTO specParamDTO);

     @ApiOperation(value = "增加规格参数")
     @DeleteMapping(value = "specparam/delSpecParam")
     Result<JSONObject> delSpecParam(Integer id);


}
