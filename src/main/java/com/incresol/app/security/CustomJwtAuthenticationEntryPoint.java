package com.incresol.app.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.incresol.app.models.HttpStatusResponse;
import com.incresol.app.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	
	
	private org.slf4j.Logger logger = LoggerFactory.getLogger(Authentication.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {

		//userService.getHttpStatusResponse(null, new HashMap<>(), 1, e.getMessage());

		PrintWriter out = response.getWriter();
		out.println("Access Denied " + e.getMessage());

	}

}
