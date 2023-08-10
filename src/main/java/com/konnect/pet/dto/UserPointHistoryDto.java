package com.konnect.pet.dto;

import java.time.LocalDateTime;

import com.konnect.pet.entity.UserPointHistory;
import com.konnect.pet.enums.code.PointHistoryTypeCode;
import com.konnect.pet.enums.code.PointTypeCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPointHistoryDto {

	private Long id;

	private int balance;

	private String pointType;
	private String pointTypeName;
	private String pointTypeSymbol;

	private String historyType;
	private String historyTypeName;

	private LocalDateTime createdDate;

	public UserPointHistoryDto(Long id, int balance, String pointType, String historyType, LocalDateTime createdDate) {

		PointTypeCode pointTypeCode = PointTypeCode.findByCode(pointType);
		PointHistoryTypeCode pointHistoryTypeCode = PointHistoryTypeCode.findByCode(historyType);

		this.id = id;
		this.balance = balance;
		this.pointType = pointTypeCode.getCode();
		this.pointTypeName = pointTypeCode.getCodeName();
		this.pointTypeSymbol = pointTypeCode.getCodeForApp();
		this.historyType = pointHistoryTypeCode.getCode();
		this.historyTypeName = pointHistoryTypeCode.getCodeForApp();
		this.createdDate = createdDate;
	}

}
