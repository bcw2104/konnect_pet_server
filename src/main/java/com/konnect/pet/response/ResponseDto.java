package com.konnect.pet.response;

import com.konnect.pet.enums.EnumModel;
import com.konnect.pet.enums.ResponseType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto {
	private String code;
	private String msg;
	private Object data;

	public ResponseDto(ResponseType responseType) {
		this.code = responseType.getKey();
		this.msg = responseType.getValue();
	}

	public ResponseDto(ResponseType responseType, Object data) {
		this.code = responseType.getKey();
		this.msg = responseType.getValue();
		this.data = data;
	}

	public ResponseDto(String code, String msg) {
		this.code = code;
		this.msg = msg;
		this.data = null;
	}

	public ResponseDto(String code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
}
