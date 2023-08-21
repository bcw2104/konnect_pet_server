package com.konnect.pet.dto;

import com.konnect.pet.entity.Faq;
import com.konnect.pet.enums.code.NotificationCategoryCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqDto {

	private Long faqId;

	private String category;
	private String categoryName;

	private String question;

	private String answer;

	public FaqDto(Faq faq) {
		this.faqId = faq.getId();
		this.category = faq.getCategory();
		this.categoryName = NotificationCategoryCode.findByCode(faq.getCategory()).getCodeForApp();
		this.question = faq.getQuestion();
		this.answer = faq.getAnswer();
	}

}
