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

	public UserPointHistoryDto(UserPointHistory history) {
		PointTypeCode pointTypeCode = PointTypeCode.findByCode(history.getPointType());
		PointHistoryTypeCode pointHistoryTypeCode = PointHistoryTypeCode.findByCode(history.getHistoryType());

		this.id = history.getId();
		this.balance = history.getBalance();
		this.pointType = pointTypeCode.getCode();
		this.pointTypeName = pointTypeCode.getCodeName();
		this.pointTypeSymbol = pointTypeCode.getCodeForApp();
		this.historyType = pointHistoryTypeCode.getCode();
		this.historyTypeName = pointHistoryTypeCode.getCodeForApp();
		this.createdDate = history.getCreatedDate();
	}

}
