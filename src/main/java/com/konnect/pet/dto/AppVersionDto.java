package com.konnect.pet.dto;

import java.time.LocalDateTime;

import com.konnect.pet.entity.AppVersion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppVersionDto {

	private String version;

	private boolean forcedYn;

	private LocalDateTime releasedDate;

	public AppVersionDto(AppVersion version) {
		this.version = version.getVersion();
		this.forcedYn = version.isForcedYn();
		this.releasedDate = version.getReleasedDate();
	}

}
