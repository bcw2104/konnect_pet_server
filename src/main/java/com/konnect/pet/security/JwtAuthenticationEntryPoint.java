package com.konnect.pet.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final HandlerExceptionResolver resolver;

	public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
    	String exception = request.getAttribute("token_ex") != null ? request.getAttribute("token_ex").toString() : "";

    	if(exception.equals(ResponseType.INVALID_ACCESS_TOKEN.getCode())) {
        	resolver.resolveException(request, response, null, new CustomResponseException(ResponseType.INVALID_ACCESS_TOKEN));
    	}
    	else {
        	resolver.resolveException(request, response, null, authException);
    	}
    }
}