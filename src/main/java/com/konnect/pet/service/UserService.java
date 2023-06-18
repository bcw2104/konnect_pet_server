package com.konnect.pet.service;

import java.util.Map;

import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.stereotype.Service;

import com.konnect.pet.dto.SocialUserInfoDto;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import com.konnect.pet.ex.CustomResponseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final ExternalApiService apiService;

}
