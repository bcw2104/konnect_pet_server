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
	private Long faqId;

	private String category;
	private String categoryName;

	private String title;

	private String question;

	private String answer;

	private LocalDateTime answeredDate;
	
	public QnaDto(Qna qna) {
		this.faqId = qna.getId();
		this.category = qna.getCategory();
		this.categoryName = ServiceCategoryCode.findByCode(qna.getCategory()).getCodeForApp();
		this.title = qna.getTitle();
		this.question = qna.getQuestion();
		this.answer = qna.getAnswer();
		this.answeredDate = qna.getAnsweredDate();
	}
	
	public QnaDto(Long faqId, String category, String title, LocalDateTime answeredDate) {
		this.faqId = faqId;
		this.category = category;
		this.categoryName = ServiceCategoryCode.findByCode(category).getCodeForApp();
		this.title = title;
		this.answeredDate = answeredDate;
	}
}
