package com.konnect.pet.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.konnect.pet.enums.ResponseType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1. Request Header 에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        String requestURI = request.getRequestURI();

        if(token != null) {
	        // 2. validateToken 으로 토큰 유효성 검사
	        ResponseType validation = jwtTokenProvider.validateToken(token, requestURI);
	        if (validation.equals(ResponseType.SUCCESS)) {

	            // 토큰이 유효r할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
	            Authentication authentication = jwtTokenProvider.getAuthentication(token);
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	        }
	        else {
	            request.setAttribute("token_ex", validation.getCode());
	        }
        }

        chain.doFilter(request, response);
    }


}