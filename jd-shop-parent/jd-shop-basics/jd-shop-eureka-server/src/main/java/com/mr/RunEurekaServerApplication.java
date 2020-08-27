package com.mr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @version 1.0
 * @author:王双全
 * @date: 2020-30-27 15:30
 */
@SpringBootApplication
@EnableEurekaServer
public class RunEurekaServerApplication {

    public static void main(String[] args) {
        System.out.println("提交成功");
        SpringApplication.run(RunEurekaServerApplication.class);
    }
}
