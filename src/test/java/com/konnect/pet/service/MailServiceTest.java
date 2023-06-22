package com.konnect.pet.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.konnect.pet.dto.MailDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MailServiceTest {

	@Autowired
	MailService mailService;

	@Test
	void mailTest() {
		Map<String, Object> data = new HashMap();
		
		data.put("sample","안녕!!");
		
		MailDto mailDto = MailDto.builder().subject("Test").receiver("bcw2104@naver.com")
				.template("email_auth_template").data(data).build();
		
		mailService.sendTemplateMail(mailDto);
	}
}
