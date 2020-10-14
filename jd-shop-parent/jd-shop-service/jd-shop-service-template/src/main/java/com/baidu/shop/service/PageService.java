package com.baidu.shop.service;

import java.util.Map;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/23 20:55
 */
public interface PageService {
    Map<String,Object> getPageInfoBySpuId(Integer spuId);
}
