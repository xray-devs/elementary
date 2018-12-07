package com.elementary.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * @author  Mr丶s
 * @ClassName  注册中心
 * @Version   V1.0
 * @Date   2018/12/7 15:31
 * @Description
 */
@EnableEurekaServer
@SpringBootApplication
public class ElementaryEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElementaryEurekaApplication.class, args);
	}
}
