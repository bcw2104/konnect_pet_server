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

	@Test
	void generatePassword() throws NoSuchAlgorithmException, NoSuchPaddingException {
		  byte[] iv = new byte[32];
		  new SecureRandom().nextBytes(iv);
		System.out.println("=====================");
			System.out.println(Base64.encodeBase64String(iv));
		System.out.println("=====================");
	}

	@Test
	void smsSendTest() {
		ResponseDto result = authService.sendVerifyCodeBySms("821050267047", LocationCode.SIGNUP);

		assertThat(result.getRsp_code()).isEqualTo("1000");

		Long id = Long.parseLong(((Map<String, Object>) result.getResult()).get("reqId").toString());
		Optional<SmsVerifyLog> smsLog = smsVerifyLogRepository.findById(id);

		assertThat(smsLog.isPresent()).isEqualTo(true);
		System.out.println(smsLog.get().toString());
	}

	@Test
	void sendMailTest() {
		Map<String, Object> data = new HashMap<>();

		data.put("sample","안녕!!");

		MailRequestDto mailDto = MailRequestDto.builder().subject("Test").receiver("bcw2104@naver.com")
				.template("email_verify_template").data(data).build();

		mailService.sendTemplateMail(mailDto);
	}
}
