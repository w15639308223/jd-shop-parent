package com.baidu.shop.mapper;

import com.baidu.shop.entity.SpuEntity;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/7 15:25
 */
public interface SpuMapper extends Mapper<SpuEntity>{
    @Update(value = "UPDATE tb_spu SET saleable = #{saleable} WHERE id = #{spuId}")
    void updateByPrimaryss(Integer spuId, Integer saleable);
}
