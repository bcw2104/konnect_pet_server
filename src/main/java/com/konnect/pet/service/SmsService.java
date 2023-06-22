package com.konnect.pet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.konnect.pet.response.ResponseDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SmsService {

	@Value("${application.twilio.sid}")
	private String TWILIO_ACCOUNT_SID;

	@Value("${application.twilio.secret}")
	private String TWILIO_ACCOUNT_SECRET;

	@Value("${application.twilio.verify.sid}")
	private String TWILIO_VERIFY_SID;

	@Value("${application.twilio.sms.sid}")
	private String TWILIO_SMS_SID;

	public boolean sendSimpleSms(String to, String content) {
		initTwilio();
		Message message = Message
				.creator(new PhoneNumber(to), TWILIO_SMS_SID, content)
//				.setStatusCallback(content)
				.create();

		log.info("call twilio sms api - status: {}, errorCode: {}", message.getStatus(),message.getErrorCode());

		if(message.getErrorCode() != null) {
			return false;
		}

		return true;
	}

	private void initTwilio() {
		Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_ACCOUNT_SECRET);
	}

}