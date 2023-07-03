package com.konnect.pet.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.MailRequestDto;
import com.konnect.pet.entity.SmsVerifyLog;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.repository.MailVerifyLogRepository;
import com.konnect.pet.repository.SmsVerifyLogRepository;
import com.konnect.pet.response.ResponseDto;

@Transactional
@SpringBootTest
public class AuthServiceTest {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthService authService;
	@Autowired
	MailService mailService;


	@Autowired
	MailVerifyLogRepository mailVerifyLogRepository;
	@Autowired
	SmsVerifyLogRepository smsVerifyLogRepository;

	
}
