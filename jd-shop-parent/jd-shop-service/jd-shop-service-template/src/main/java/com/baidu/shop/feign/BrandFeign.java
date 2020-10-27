package com.baidu.shop.feign;

import com.baidu.shop.business.BrandService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/21 15:03
 */
@FeignClient(contextId = "BrandService",value = "xxx-service")
public interface BrandFeign extends BrandService {
}
