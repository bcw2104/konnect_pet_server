package com.konnect.pet.dto;

import java.time.LocalDateTime;

import com.konnect.pet.enums.VerifyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyFormat {

	private String target;
	private VerifyType type;
	private LocalDateTime verifyTime;
	private Long verifyId;
}
