package com.baidu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.base.BaseApiService;
import com.baidu.base.Result;
import com.baidu.dto.SpecGroupDTO;
import com.baidu.entity.SpecGroupEntity;
import com.baidu.mapper.SpecGroupMapper;
import com.baidu.service.SpecGroupService;
import com.baidu.utils.BaiduBeanUtil;
import com.baidu.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/3 12:00
 */
@RestController
public class SpecGroupServiceImpl extends BaseApiService implements SpecGroupService{

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Transactional
    @Override
    public Result<List<SpecGroupEntity>> getSpecGroupInfo(SpecGroupDTO specGroupDTO) {

        //通过分类id查询数据
        Example example = new Example(SpecGroupEntity.class);
        if (ObjectUtil.isNOtNull(specGroupDTO.getId()))
            example.createCriteria().andEqualTo("cid",specGroupDTO.getId());
        List<SpecGroupEntity> list = specGroupMapper.selectByExample(example);
        return this.setResultSuccess(list);
    }

    @Transactional
    @Override
    public Result<JSONObject> saveSpecGroupInfo(SpecGroupDTO specGroupDTO) {
        specGroupMapper.insertSelective(BaiduBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> editSpecGroupInfo(SpecGroupDTO specGroupDTO) {

        specGroupMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class));
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> delSpecGroupInfo(Integer id) {
        specGroupMapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
}