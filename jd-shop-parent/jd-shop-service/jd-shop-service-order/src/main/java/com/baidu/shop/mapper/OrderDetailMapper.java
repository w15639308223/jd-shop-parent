package com.baidu.shop.mapper;


import com.baidu.shop.entity.OrderDetailEntity;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/21 15:41
 */
public interface OrderDetailMapper extends Mapper<OrderDetailEntity>, InsertListMapper<OrderDetailEntity> {
}
