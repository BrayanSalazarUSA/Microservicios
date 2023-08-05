package com.microservicios.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableCircuitBreaker//se encarga mediante un hilo separado de la comunicacion de los 
//microservicios, para tolerancia a fallos,manejo de latencias y timeout.  esto va a envolver a ribbon
@EnableEurekaClient
//@RibbonClient(name="servicio-productos")
@EnableFeignClients//para habilitar los clientes feign en el proyecto y luego poder inyectarlos
@SpringBootApplication
@EntityScan({"com.springboot.app.commons.models.entity"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemApplication.class, args);
	}

}
