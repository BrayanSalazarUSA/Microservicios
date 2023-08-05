package com.microservicios.item;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class AppConfig{

    @Bean("clienteRest")
    @LoadBalanced//va a utilizar ribbon para el balanceo de carga
    public RestTemplate registrarRestTemplate(){
        //el rest template es un cliente http para poder trabajar con recursos que estan en otros microserv
        return new RestTemplate();
    }
    
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizar(){
    		//retornamos un a expresion lambda
    	return factory -> factory.configureDefault(id -> {
    		return new Resilience4JConfigBuilder(id)
    				.circuitBreakerConfig(CircuitBreakerConfig.custom()
    						.slidingWindowSize(10)
    						.failureRateThreshold(50)
    						.waitDurationInOpenState(Duration.ofSeconds(10L))
    						.permittedNumberOfCallsInHalfOpenState(5)
    						.slowCallRateThreshold(50) //configura en porcentajes el umbral de llamadas lentas
    						.slowCallDurationThreshold(Duration.ofSeconds(2L))//se considera lenta si es mayor que 2S
    						.build())
    				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3L)).build()).build();
    	});
    	
    }
}
