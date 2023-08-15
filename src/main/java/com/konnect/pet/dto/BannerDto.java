package com.konnect.pet.dto;

import com.konnect.pet.entity.Banner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerDto {

	private Long bannerId;
	private String imgUrl;
	private String landingUrl;

	public BannerDto(Banner banner) {
		this.bannerId = banner.getId();
		this.imgUrl = banner.getImgUrl();
		this.landingUrl = banner.getLandingUrl();
	}


}
