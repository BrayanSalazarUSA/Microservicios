package com.springboot.serviciogatewayserver.filters.factory;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.springboot.serviciogatewayserver.filters.EjemploGlobalFilter;

import reactor.core.publisher.Mono;

@Component
public class EjemploGatewayFilterFactory
		extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> {
	
	private final Logger log = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);
	
	
	public EjemploGatewayFilterFactory() {
		super(Configuracion.class);
	}


	@Override
	public GatewayFilter apply(Configuracion config) {

		return (exchange, chain) -> {
			
			log.info("ejecutando pre gateway filter factory: "+config.mensaje);
			
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				
				Optional.ofNullable(config.cookieValor).ifPresent(cookie -> {
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
				});
				
				log.info("ejecutando post gateway filter factory: "+config.mensaje);
			}));
		};
	}
	
	public static class Configuracion {
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public String getCookieValor() {
			return cookieValor;
		}
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		public String getCookieNombre() {
			return cookieNombre;
		}
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}
		
	}

}
