package com.baidu.shop.mapper;

import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.entity.SkuEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/8 19:59
 */

public interface SkuMapper extends Mapper<SkuEntity> , DeleteByIdListMapper<SkuEntity,Long> {
    @Select(value = "select k.*,k.own_spec as ownSpec,stock "+"from tb_sku k , tb_stock t where k.id = t.sku_id and k.spu_id=#{spuId}")
    List<SkuDTO> getSkuBySpu(Integer spuId);
}
