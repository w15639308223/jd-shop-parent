package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SpecGroupDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.entity.SpecGroupEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.mapper.SpecGroupMapper;
import com.baidu.shop.mapper.SpecParamMapper;
import com.baidu.shop.service.SpecGroupService;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/3 12:00
 */
@RestController
public class SpecGroupServiceImpl extends BaseApiService implements SpecGroupService{

    @Resource
    private SpecGroupMapper specGroupMapper;

    @Resource
    private SpecParamMapper specParamMapper;

    @Override
    public Result<List<SpecGroupEntity>> getSpecGroupInfo(SpecGroupDTO specGroupDTO) {
        //通过分类id查询数据
        Example example = new Example(SpecGroupEntity.class);
        if (ObjectUtil.isNOtNull(specGroupDTO.getCid()))
            example.createCriteria().andEqualTo("cid",specGroupDTO.getCid());
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

    @Transactional
    @Override
    public Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO) {

        //if (ObjectUtil.isNull(specParamDTO.getGroupId())) return this.setResultSuccess("规格组id不能为空");

        Example example = new Example(SpecParamEntity.class);
        //example.createCriteria().andEqualTo("groupId",specParamDTO.getGroupId());
        Example.Criteria criteria = example.createCriteria();

        if (ObjectUtil.isNOtNull(specParamDTO.getGroupId())){
            criteria.andEqualTo("groupId",specParamDTO.getGroupId());
        }

        if (ObjectUtil.isNOtNull(specParamDTO.getCid())){
            criteria.andEqualTo("cid",specParamDTO.getCid());
        }

        if (ObjectUtil.isNOtNull(specParamDTO.getSearching())){
            criteria.andEqualTo("searching",specParamDTO.getSearching());
        }

        List<SpecParamEntity> list = specParamMapper.selectByExample(example);

        return this.setResultSuccess(list);
    }

    @Transactional
    @Override
    public Result<JSONObject> saveSpecParam(SpecParamDTO specParamDTO) {
        specParamMapper.insertSelective(BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> editSpecParam(SpecParamDTO specParamDTO) {
        specParamMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> delSpecParam(Integer id) {
        if (ObjectUtil.isNOtNull(id)){

            specParamMapper.deleteByPrimaryKey(id);
        }
        return this.setResultSuccess();
    }
}
