package com.konnect.pet.dto;

import com.konnect.pet.entity.CommunityCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityCategoryDto {
	private Long categoryId;

	private String category;

	public CommunityCategoryDto(CommunityCategory category) {
		this.categoryId = category.getId();
		this.category = category.getCategory();
	}

}
