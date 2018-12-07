package com.elementary.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author  Mr丶s
 * @ClassName  登录模块
 * @Version   V1.0
 * @Date   2018/12/7 15:41
 * @Description
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ElementaryRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElementaryRegisterApplication.class, args);
	}
}
