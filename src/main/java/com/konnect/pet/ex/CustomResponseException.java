package com.konnect.pet.ex;

import com.konnect.pet.enums.ResponseType;

import lombok.Getter;

@Getter
public class CustomResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ResponseType responseType;

	public CustomResponseException() {
		this.responseType = ResponseType.FAIL;
	}

	public CustomResponseException(ResponseType responseType) {
		this.responseType = responseType;
	}

	public CustomResponseException(ResponseType responseType, String message) {
		super(message);
		this.responseType = responseType;
	}

	public CustomResponseException(ResponseType responseType, String message, Throwable cause) {
        super(message, cause);
		this.responseType = responseType;
    }
}
