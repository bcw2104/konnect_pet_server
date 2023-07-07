package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnect.pet.dto.MailRequestDto;
import com.konnect.pet.dto.VerifyFormat;
import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.SmsVerifyLog;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.MailVerifyLogRepository;
import com.konnect.pet.repository.SmsVerifyLogRepository;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.utils.Aes256Utils;
import com.konnect.pet.utils.ValidationUtils;
import com.twilio.rest.api.v2010.account.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyService {

	private final MailService mailService;
	private final SmsService smsService;
	private final ObjectMapper objectMapper;

	private final MailVerifyLogRepository mailVerifyLogRepository;
	private final SmsVerifyLogRepository smsVerifyLogRepository;

	@Value("${application.name}")
	private String APP_NAME;
	@Value("${application.aes.privacy.key}")
	private String PRIVACY_AES_KEY;
	@Value("${application.aes.privacy.iv}")
	private String PRIVACY_AES_IV;

	// seconds
	private final int SMS_VERIFY_TIMEOUT = 180;
	private final int EMAIL_VERIFY_TIMEOUT = 600;

	@Transactional
	public ResponseDto sendVerifyCodeBySms(String tel, LocationCode locationCode) {
		String verifiyCode = ValidationUtils.generateRandomString(6, true, false);
		StringBuffer smsContent = new StringBuffer();
		smsContent.append("Your ").append(APP_NAME).append(" verification code is: ").append(verifiyCode);

		Message message = smsService.sendSimpleSms(tel, smsContent.toString());

		if (message != null) {

			SmsVerifyLog verifyLog = SmsVerifyLog.builder().verifiyCode(verifiyCode).consumedYn(false).successYn(false)
					.locationCode(locationCode.getCode()).build();

			smsVerifyLogRepository.save(verifyLog);

			Map<String, Object> result = new HashMap<>();
			result.put("reqId", verifyLog.getId());
			result.put("tel", message.getTo());
			result.put("timestamp",
					verifyLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

			return new ResponseDto(ResponseType.SUCCESS, result);
		} else {
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}

	}


	@Transactional
	public ResponseDto sendVerifyCodeByEmail(String email, LocationCode locationCode) {
		Map<String, Object> mailData = new HashMap<>();

		String verifiyCode = ValidationUtils.generateRandomString(8, true, true);

		mailData.put("verifyCode", verifiyCode);

		MailRequestDto mail = MailRequestDto.builder().subject("Konnect - Email authentication code for sign up")
				.receiver(email).template("email_verify_template").data(mailData).build();

		mailService.sendTemplateMail(mail);

		MailVerifyLog verifyLog = MailVerifyLog.builder().verifiyCode(verifiyCode).consumedYn(false)
				.locationCode(locationCode.getCode()).build();

		mailVerifyLogRepository.save(verifyLog);

		Map<String, Object> result = new HashMap<>();
		result.put("reqId", verifyLog.getId());
		result.put("timestamp", verifyLog.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

		return new ResponseDto(ResponseType.SUCCESS, result);
	}


	@Transactional
	public ResponseDto validateVerfiyCode(Long logId, LocalDateTime timestamp, String verifyCode, String target,
			VerifyType type) {
		LocalDateTime now = LocalDateTime.now();

		if (VerifyType.SMS.equals(type)) {
			SmsVerifyLog verifyLog = smsVerifyLogRepository.findById(logId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			if (verifyLog.isConsumedYn()) {
				new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
			if (!timestamp.isEqual(verifyLog.getCreatedDate())) {
				new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
			if (verifyLog.getCreatedDate().plusSeconds(SMS_VERIFY_TIMEOUT).isBefore(now)) {
				throw new CustomResponseException(ResponseType.VERIFY_TIMEOUT);
			}
			if (!verifyLog.getVerifiyCode().equals(verifyCode)) {
				throw new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
			verifyLog.setConsumedYn(true);
		} else if (VerifyType.EMAIL.equals(type)) {
			MailVerifyLog verifyLog = mailVerifyLogRepository.findById(logId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			if (verifyLog.isConsumedYn()) {
				new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
			if (!timestamp.isEqual(verifyLog.getCreatedDate())) {
				new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}
			if (verifyLog.getCreatedDate().plusSeconds(EMAIL_VERIFY_TIMEOUT).isBefore(now)) {
				throw new CustomResponseException(ResponseType.VERIFY_TIMEOUT);
			}
			if (!verifyLog.getVerifiyCode().equals(verifyCode)) {
				throw new CustomResponseException(ResponseType.VERIFY_FAIL);
			}
			verifyLog.setConsumedYn(true);
		} else {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		VerifyFormat format = VerifyFormat.builder().verifyId(logId).verifyTime(timestamp).type(type).target(target)
				.build();

		try {
			String data = objectMapper.writeValueAsString(format);

			Map<String, Object> result = new HashMap<>();
			result.put("key", Aes256Utils.encrypt(data, PRIVACY_AES_KEY, PRIVACY_AES_IV));

			return new ResponseDto(ResponseType.SUCCESS, result);
		} catch (Exception e) {
			log.error("validate verfiy error - {}", e.getMessage(), e);
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}
	}

	@Transactional(readOnly = true)
	public VerifyFormat validateVerfiyKey(String key) {
		if(key == null) {
			new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		try {
			String decKey = Aes256Utils.decrypt(key, PRIVACY_AES_KEY, PRIVACY_AES_IV);

			VerifyFormat format = objectMapper.readValue(decKey, VerifyFormat.class);

			if (VerifyType.SMS.equals(format.getType())) {
				SmsVerifyLog verifyLog = smsVerifyLogRepository.findById(format.getVerifyId())
						.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

				if (!verifyLog.isConsumedYn() || !format.getVerifyTime().isEqual(verifyLog.getCreatedDate())) {
					new CustomResponseException(ResponseType.INVALID_PARAMETER);
				}
			} else if (VerifyType.EMAIL.equals(format.getType())) {
				MailVerifyLog verifyLog = mailVerifyLogRepository.findById(format.getVerifyId())
						.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

				if (!verifyLog.isConsumedYn() || !format.getVerifyTime().isEqual(verifyLog.getCreatedDate())) {
					new CustomResponseException(ResponseType.INVALID_PARAMETER);
				}
			} else if (VerifyType.SOCIAL.equals(format.getType())) {

			} else {
				throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}

			return format;
		} catch (Exception e) {
			log.error("validate key error - {}", e.getMessage(), e);
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}
	}
}
