package com.baidu.shop.feign;

import com.baidu.shop.service.SpecGroupService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/17 17:37
 */
@FeignClient(contextId = "",value = "xxx-service")
public interface SpecificationFeign  extends SpecGroupService {
}
