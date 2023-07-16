package com.konnect.pet.ex;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.response.ResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
		log.error("message: {}, class: {}", e.getMessage(), AccessDeniedException.class, e);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(ResponseType.INVALID_ACCESS_TOKEN));
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
		log.error("message: {}, class: {}", e.getMessage(), AuthenticationException.class, e);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto(ResponseType.UNAUTHORIZED));
	}

	@ExceptionHandler(CustomResponseException.class)
	protected ResponseEntity<?> handleCustomResponseException(CustomResponseException e) {
		log.error("message: {}, rep_code:{}, rep_msg:{}, class: {}", e.getMessage(), e.getResponseType().getCode(),
				e.getResponseType().getMessage(), CustomResponseException.class);
		return ResponseEntity.status(e.getStatus()).body(new ResponseDto(e.getResponseType()));
	}

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<?> handleRuntimeException(RuntimeException e) {
		log.error("message: {}, class: {}", e.getMessage(), RuntimeException.class, e);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(ResponseType.FAIL));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<?> handleException(Exception e) {
		log.error("message: {}, class: {}", e.getMessage(), Exception.class, e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(ResponseType.SERVER_ERROR));
	}

}
