package com.konnect.pet.response;

import com.konnect.pet.enums.ResponseType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto {
	private String rsp_code;
	private String rsp_msg;
	private String rsp_msg_detail;
	private Object result;

	public ResponseDto(ResponseType responseType) {
		this.rsp_code = responseType.getCode();
		this.rsp_msg = responseType.getMessage();
		this.rsp_msg_detail = responseType.getDisplayMessage();
	}

	public ResponseDto(ResponseType responseType, Object result) {
		this.rsp_code = responseType.getCode();
		this.rsp_msg = responseType.getMessage();
		this.rsp_msg_detail = responseType.getDisplayMessage();
		this.result = result;
	}

	public ResponseDto(String rsp_code, String msg) {
		this.rsp_code = rsp_code;
		this.rsp_msg = msg;
		this.result = null;
	}

	public ResponseDto(String rsp_code, String rsp_msg, Object result) {
		this.rsp_code = rsp_code;
		this.rsp_msg = rsp_msg;
		this.result = result;
	}
}
