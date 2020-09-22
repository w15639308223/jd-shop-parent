package com.baidu.shop.mapper;

import com.baidu.shop.entity.StockEntity;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/8 19:59L
 */
public interface StockMapper extends Mapper<StockEntity> , DeleteByIdListMapper<StockEntity,Long>{
}
