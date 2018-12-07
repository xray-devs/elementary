package com.elementary.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author   Mr丶s
 * @ClassName  启动类
 * @Version   V1.0
 * @Date   2018/12/6 16:37
 * @Description
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ElementaryCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElementaryCoreApplication.class, args);
    }
}
