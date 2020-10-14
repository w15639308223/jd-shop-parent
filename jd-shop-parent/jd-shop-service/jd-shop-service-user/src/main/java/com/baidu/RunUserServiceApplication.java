package com.baidu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/10/13 15:13
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = "com.baidu.shop.mapper")
public class RunUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunUserServiceApplication.class);
    }
}
