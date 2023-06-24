package com.konnect.pet.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.MailDto;
import com.konnect.pet.entity.SmsVerifyLog;
import com.konnect.pet.enums.code.VerifyLocationCode;
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
	void generatePassword() {
		System.out.println(passwordEncoder.encode("1111111"));
	}

	@Test
	void smsSendTest() {
		ResponseDto result = authService.sendVerifyCodeBySms("821050267047", VerifyLocationCode.SIGNUP);

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

		MailDto mailDto = MailDto.builder().subject("Test").receiver("bcw2104@naver.com")
				.template("email_verify_template").data(data).build();

		mailService.sendTemplateMail(mailDto);
	}
}
