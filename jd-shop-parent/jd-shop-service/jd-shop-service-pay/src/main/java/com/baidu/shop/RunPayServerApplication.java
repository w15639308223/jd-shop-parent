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
 * @date: 2020/10/22 15:45
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据库配置
@EnableEurekaClient
@EnableFeignClients
public class RunPayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunPayServerApplication.class);
    }
}
