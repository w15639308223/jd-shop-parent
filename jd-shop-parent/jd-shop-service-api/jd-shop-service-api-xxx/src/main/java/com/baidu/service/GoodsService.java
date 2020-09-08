package com.baidu.service;

import com.baidu.base.Result;
import com.baidu.dto.SpuDTO;
import com.baidu.entity.SpuEntity;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/7 15:14
 */
@Api(tags = "商品接口")
public interface GoodsService {

    @ApiOperation(value = "获取spu信息")
    @GetMapping(value = "goods/getSpuInfo")
     Result<PageInfo<SpuEntity>> getSpuInfo(SpuDTO spuDTO);
}
