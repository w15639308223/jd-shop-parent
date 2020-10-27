package com.baidu.shop.feign;

import com.baidu.shop.business.CategoryService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/21 15:05
 */
@FeignClient(contextId = "CategoryService",value = "xxx-service")
public interface CategoryFeign  extends CategoryService {
}
