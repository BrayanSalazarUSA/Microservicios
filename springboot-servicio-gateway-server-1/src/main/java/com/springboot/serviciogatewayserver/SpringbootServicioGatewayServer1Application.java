package com.springboot.serviciogatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SpringbootServicioGatewayServer1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioGatewayServer1Application.class, args);
	}

}
