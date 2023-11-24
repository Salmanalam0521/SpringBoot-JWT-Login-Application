package com.incresol.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORS_Config {
//	@Bean
//	public CorsFilter corsFilter() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration config = new CorsConfiguration();
//		config.addAllowedOrigin("http://localhost:4200");
//		config.addAllowedHeader("*");
//		config.addAllowedMethod("GET");
//		config.addAllowedMethod("POST");
//		config.addAllowedMethod("PUT");
//		config.addAllowedMethod("DELETE");
//		config.addAllowedMethod("OPTIONS");
//		source.registerCorsConfiguration("/**", config);
//	    return new CorsFilter((CorsConfigurationSource) source);
//
//	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override          //.exposedHeaders("Custom-Header")
			public void addCorsMappings(CorsRegistry registry) { 
				registry.addMapping("/*").allowedOrigins("*")
						.allowedMethods("*").allowCredentials(true)
						.allowedHeaders("*")
						; // Set to true
																											// to allow
																											// credentials

			}
		};
	}

}
