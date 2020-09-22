package com.baidu.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/9/1 20:23
 */
@SpringBootApplication
@EnableEurekaClient
public class RunUploadServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunUploadServerApplication.class);
    }
}
