package com.konnect.pet.dto;

import com.konnect.pet.entity.UserPoint;
import com.konnect.pet.enums.code.PointTypeCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPointDto {
	private int balance;

	private String pointType;
	private String pointTypeName;
	private String pointTypeSymbol;

	public UserPointDto(PointTypeCode typeCode) {
		this.balance = 0;
		this.pointType = typeCode.getCode();
		this.pointTypeName = typeCode.getCodeName();
		this.pointTypeSymbol = typeCode.getCodeForApp();
	}

	public UserPointDto(UserPoint userPoint) {
		PointTypeCode pointTypeCode = PointTypeCode.findByCode(userPoint.getPointType());
		this.balance = userPoint.getBalance();
		this.pointType = userPoint.getPointType();
		this.pointTypeName = pointTypeCode.getCodeName();
		this.pointTypeSymbol = pointTypeCode.getCodeForApp();
	}

}
