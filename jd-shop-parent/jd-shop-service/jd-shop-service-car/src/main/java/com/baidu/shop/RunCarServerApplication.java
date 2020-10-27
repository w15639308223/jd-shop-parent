package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/19 20:46
 */
//需要自定义数据源的时候，或者不想系统自动注入数据源的时候，就把它排除掉。
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableEurekaClient
public class RunCarServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunCarServerApplication.class);
    }
}
