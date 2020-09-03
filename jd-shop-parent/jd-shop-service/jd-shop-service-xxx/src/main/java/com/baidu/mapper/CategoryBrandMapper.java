package com.baidu.mapper;

import com.baidu.entity.CategoryBrandEntity;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/1 14:51
 */
//接口可以多继承,InsertListMapper用于批量新增
public interface CategoryBrandMapper extends Mapper<CategoryBrandEntity>, InsertListMapper<CategoryBrandEntity> {
}
