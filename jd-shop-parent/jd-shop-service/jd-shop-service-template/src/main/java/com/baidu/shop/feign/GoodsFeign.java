package com.baidu.shop.feign;

import com.baidu.shop.business.GoodsService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "GoodsService",value = "xxx-service")
public interface GoodsFeign extends GoodsService {
}