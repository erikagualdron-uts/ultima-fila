package com.utsproyecto.ultimafila.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registro) {
		registro.addInterceptor(new InterceptorSesion()).addPathPatterns(InterceptorSesion.prefijosProtegidos());
	}
}
