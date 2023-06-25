package com.konnect.pet.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.konnect.pet.enums.PlatformType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

	private PlatformType platform;
	private Long smsReqId;
	private String smsTimestamp;
	private String tel;

	private Long emailReqId;
	private String emailTimestamp;
	private String email;

	private String platformId;
	private String password;

	private Map<Long, Object> termsAgreed;

	public String getMaskingTel() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.tel.substring(0, 2));
		sb.append("****");
		sb.append(this.tel.substring(6));
		return sb.toString();
	}
}


