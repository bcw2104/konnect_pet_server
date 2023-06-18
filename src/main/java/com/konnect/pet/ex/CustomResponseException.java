package com.konnect.pet.ex;

import org.springframework.http.HttpStatus;

import com.konnect.pet.enums.ResponseType;

import lombok.Getter;

@Getter
public class CustomResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private HttpStatus status;
	private ResponseType responseType;

	public CustomResponseException() {
		this.status = HttpStatus.BAD_REQUEST;
		this.responseType = ResponseType.FAIL;
	}

	public CustomResponseException(ResponseType responseType) {
		this.status = HttpStatus.BAD_REQUEST;
		this.responseType = responseType;
	}

	public CustomResponseException(ResponseType responseType, String message) {
		super(message);
		this.status = HttpStatus.BAD_REQUEST;
		this.responseType = responseType;
	}

	public CustomResponseException(ResponseType responseType, String message, Throwable cause) {
		super(message, cause);
		this.status = HttpStatus.BAD_REQUEST;
		this.responseType = responseType;
	}

	public CustomResponseException(HttpStatus status) {
		this.status = status;
		this.responseType = ResponseType.FAIL;
	}

	public CustomResponseException(HttpStatus status, ResponseType responseType) {
		this.status = status;
		this.responseType = responseType;
	}

	public CustomResponseException(HttpStatus status, ResponseType responseType, String message) {
		super(message);
		this.status = status;
		this.responseType = responseType;
	}

	public CustomResponseException(HttpStatus status, ResponseType responseType, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
		this.responseType = responseType;
	}
}
