package com.konnect.pet.dto;

import java.time.LocalDateTime;

import com.konnect.pet.entity.Qna;
import com.konnect.pet.enums.code.NotificationCategoryCode;
import com.konnect.pet.enums.code.ServiceCategoryCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaDto {
	private Long qnaId;

	private String category;
	private String categoryName;

	private String title;

	private String question;

	private String answer;

	private LocalDateTime createdDate;
	private LocalDateTime answeredDate;
	
	public QnaDto(Qna qna) {
		this.qnaId = qna.getId();
		this.category = qna.getCategory();
		this.categoryName = ServiceCategoryCode.findByCode(qna.getCategory()).getCodeForApp();
		this.title = qna.getTitle();
		this.question = qna.getQuestion();
		this.answer = qna.getAnswer();
		this.createdDate = qna.getCreatedDate();
		this.answeredDate = qna.getAnsweredDate();
	}
	
	public QnaDto(Long qnaId, String category, String title,LocalDateTime createdDate, LocalDateTime answeredDate) {
		this.qnaId = qnaId;
		this.category = category;
		this.categoryName = ServiceCategoryCode.findByCode(category).getCodeForApp();
		this.title = title;
		this.createdDate = createdDate;
		this.answeredDate = answeredDate;
	}
}
