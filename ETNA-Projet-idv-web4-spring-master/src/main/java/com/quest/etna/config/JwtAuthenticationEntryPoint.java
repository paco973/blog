package com.quest.etna.config;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable{
	private static final long serialVersionUID = 1L;
	
	@Override
	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
	                     AuthenticationException authenticationException) throws IOException {
	    httpServletResponse.setContentType("application/json");
	    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    httpServletResponse.getOutputStream().println("{ \"error\": \"" + authenticationException.getMessage() + "\" }");
	}

}
