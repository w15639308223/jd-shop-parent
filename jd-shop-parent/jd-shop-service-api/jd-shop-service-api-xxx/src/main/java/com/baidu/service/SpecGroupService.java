package com.baidu.service;

import com.alibaba.fastjson.JSONObject;
import com.baidu.base.Result;
import com.baidu.dto.SpecGroupDTO;
import com.baidu.entity.SpecGroupEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
     Result<List<SpecGroupEntity>> getSpecGroupInfo(SpecGroupDTO specGroupDTO);

     @ApiOperation(value = "新增规格参数")
     @PostMapping(value = "specGroup/save")
     Result<JSONObject> saveSpecGroupInfo(@RequestBody SpecGroupDTO specGroupDTO);

     @ApiOperation(value = "修改规格参数")
     @PutMapping(value = "specGroup/save")
     Result<JSONObject> editSpecGroupInfo(@RequestBody SpecGroupDTO specGroupDTO);

     @ApiOperation(value = "删除规格参数")
     @DeleteMapping(value = "specGroup/del")
     Result<JSONObject> delSpecGroupInfo(Integer id);
}
