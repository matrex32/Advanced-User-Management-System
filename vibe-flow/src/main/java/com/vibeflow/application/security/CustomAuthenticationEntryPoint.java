package com.vibeflow.application.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	  @Override
	    public void commence(HttpServletRequest request, HttpServletResponse response,
	                         AuthenticationException authException) throws IOException {
		     System.out.println(request.getRequestURI());
	        if (request.getRequestURI().contains("/api/users/redirect-reset-password")) {

	        	return;
	        } else {
	            response.sendRedirect("/login");
	        }
	    }
}
