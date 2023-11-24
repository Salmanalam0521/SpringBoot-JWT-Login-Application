package com.incresol.app.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private org.slf4j.Logger logger = LoggerFactory.getLogger(Authentication.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
			throws IOException, ServletException {

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		
		Map<String,Object> errorResponse=new HashMap<>();
		errorResponse.put("statusCode", HttpServletResponse.SC_UNAUTHORIZED);
		errorResponse.put("info", "Unauthorized");
		errorResponse.put("errorCode", 16);
		errorResponse.put("message", e.getMessage());
		
		ObjectMapper objectMapper=new ObjectMapper();
		String jsonString=objectMapper.writeValueAsString(errorResponse);
		
		JsonNode jsonNode = objectMapper.readTree(jsonString);  // JsonNode to represent a JSON tree.
		System.out.println(jsonNode);
		
		response.getWriter().write(jsonString);
		response.getWriter().flush();	
	}
}
