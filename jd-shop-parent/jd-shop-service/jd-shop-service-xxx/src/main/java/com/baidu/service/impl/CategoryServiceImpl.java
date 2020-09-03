package com.baidu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.base.BaseApiService;
import com.baidu.base.Result;
import com.baidu.entity.CategoryEntity;
import com.baidu.mapper.CategoryMapper;
import com.baidu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Description: TODO
 * @Author shenyaqi
 * @Date 2020/8/17
 * @Version V1.0
 **/
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    //商品分类查询
    @Override
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {

        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setParentId(pid);

        List<CategoryEntity> list = categoryMapper.select(categoryEntity);

        return this.setResultSuccess(list);
    }

    @Transactional
    @Override
    public Result<JSONObject> postCategory(CategoryEntity categoryEntity) {

        CategoryEntity parentCategoryEntity = new CategoryEntity();
        //通过页面传递过来的parentid查询parentid对应的数据是否为父节点isParent==1
        //如果parentid对应的isParent != 1
        parentCategoryEntity.setId(categoryEntity.getParentId());
        //需要修改为1
        parentCategoryEntity.setIsParent(1);
        //通过新增节点的父id将父节点的parent状态改为1
        categoryMapper.updateByPrimaryKeySelective(parentCategoryEntity);
        //新增修改状态的pid
        categoryMapper.insertSelective(categoryEntity);
        return this.setResultSuccess();
    }

    @Transactional
    @Override
    public Result<JSONObject> putCategory(CategoryEntity categoryEntity) {
        categoryMapper.updateByPrimaryKeySelective(categoryEntity);
        return this.setResultSuccess();
    }


    @Transactional
    @Override
    public Result<JSONObject> delCategory(Integer id) {

        //通过当前id查询分类信息
        CategoryEntity categoryEntity = categoryMapper.selectByPrimaryKey(id);
        if (categoryEntity == null) {
            return this.setResultError("当前不存在");
        }
        //不能查询到分类新信息return
        if (categoryEntity.getIsParent() == 1) {
            return  this.setResultError("当前是父节点,不能删除");
        }
        //构建条件查询 通过当前被删除节点的parentid查询数据
        Example example = new Example(CategoryEntity.class);
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        List<CategoryEntity> list = categoryMapper.selectByExample(example);
        //还需要判断一下除了当前被删除的id外还有没有父id是当前节点父id的数据
        //如果没有的话 将父节点isparert的值改为0
        //如果查询的数据只有一条
        if (list.size() == 1){
            CategoryEntity entity = new CategoryEntity();
            entity.setId(categoryEntity.getParentId());
            entity.setParentId(0);
            categoryMapper.updateByPrimaryKeySelective(entity);//修改状态
        }
        categoryMapper.deleteByPrimaryKey(id);//执行删除
        return this.setResultSuccess();
    }


    @Override
    public Result<List<CategoryEntity>> getByBrand(Integer brandId) {
        List<CategoryEntity> byBrandId = categoryMapper.getByBrandId(brandId);
        return this.setResultSuccess(byBrandId);
    }

}
