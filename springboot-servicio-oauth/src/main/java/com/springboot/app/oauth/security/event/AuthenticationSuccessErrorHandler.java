package com.springboot.app.oauth.security.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.services.IUsuarioService;

import brave.Tracer;
import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private Tracer tracer;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		if (authentication.getDetails() instanceof WebAuthenticationDetails) {
			return;
		}
		UserDetails user = (UserDetails) authentication.getPrincipal();
		System.out.println("Succes Login: " + user.getUsername());
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		
		if(usuario.getIntentos() != null && usuario.getIntentos() > 0) {
			usuario.setIntentos(0);
			usuarioService.update(usuario,usuario.getId());
		}
		

	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		String mensaje = "Mensaje de error"+exception.getMessage();
		System.out.println(mensaje);
		try {
			
			StringBuilder errors = new StringBuilder();
			errors.append(mensaje);
			
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			if(usuario.getIntentos() == null) {
				usuario.setIntentos(0);
			}
			System.out.println("Intento actual es de: "+usuario.getIntentos());
			usuario.setIntentos(usuario.getIntentos() + 1);
			System.out.println("Intento despues es de: "+usuario.getIntentos());
			errors.append("-"+"Intento despues es del login: "+usuario.getIntentos());
			System.out.println("EL usuario "+usuario.getNombre()+" deshabilitado por maximos intento");
			if(usuario.getIntentos() >= 3) {
				String errorMaximosIntentos = "El usuario esta des-habilitado por maximos intentos"+usuario.getIntentos();
				System.out.println(errorMaximosIntentos);
				errors.append("-"+errorMaximosIntentos);
				usuario.setEnabled(false);
			}
			
			usuarioService.update(usuario, usuario.getId());
			
			tracer.currentSpan().tag("error.mensaje", errors.toString());
			
			} catch(FeignException e) {
				System.out.println("El usaurio no existe en el sistema: "+authentication.getName());
			}
		
	}

}
