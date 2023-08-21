package com.konnect.pet.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnect.pet.constant.CommonCodeConst;
import com.konnect.pet.dto.AppVersionDto;
import com.konnect.pet.entity.AppVersion;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.repository.AppVersionRepository;
import com.konnect.pet.repository.TermsGroupRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.UserTermsAgreementRepository;
import com.konnect.pet.repository.redis.RefreshTokenRepository;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
	private final AppVersionRepository appVersionRepository;

	public ResponseDto getAppInfo(String version) {
		Map<String, Object> result = new HashMap<String, Object>();

		List<AppVersion> lastestVersion = appVersionRepository.findTopOrderByReleasedDateDesc(PageRequest.of(0, 1));
		List<AppVersion> lastestForcedVersion = appVersionRepository.findTopByForcedYnOrderByReleasedDateDesc(true,
				PageRequest.of(0, 1));

		result.put("version", appVersionRepository.findByVersion(version).orElse(null));
		result.put("lastestVersion", lastestVersion.isEmpty() ? null : new AppVersionDto(lastestVersion.get(0)));
		result.put("lastestForcedVersion",
				lastestForcedVersion.isEmpty() ? null : new AppVersionDto(lastestForcedVersion.get(0)));

		return new ResponseDto(ResponseType.SUCCESS, result);
	}

}
