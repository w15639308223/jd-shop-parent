package com.baidu.shop.feigin;

import com.baidu.shop.base.Result;
import com.baidu.shop.dto.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/22 19:19
 */
@FeignClient(value = "order-server",contextId = "OrderService")
public interface OrderFeign {

    @PostMapping(value = "order/getOrderInfoByOrderId")
    Result<OrderInfo> getOrderInfoByOrderId(@RequestParam Long orderId);
}
