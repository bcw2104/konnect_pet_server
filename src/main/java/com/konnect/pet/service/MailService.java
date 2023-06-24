package com.konnect.pet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.konnect.pet.dto.MailDto;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender mailSender;
	private final TemplateEngine engine;

	@Value("${spring.mail.username}")
	private String MAIL_SENDER;
	@Value("${application.name}")
	private String APP_NAME;

	public void sendTemplateMail(MailDto mail) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			Context context = new Context();
			context.setVariables(mail.getData());

			String html = engine.process("mails/" + mail.getTemplate(), context);

			message.setSubject(mail.getSubject());
			message.setFrom(new InternetAddress(MAIL_SENDER, APP_NAME));
			message.addRecipient(RecipientType.TO, new InternetAddress(mail.getReceiver()));
			message.setText(html, "UTF-8", "html");
			mailSender.send(message);
		} catch (Exception e) {
			log.info("Mail send error - to: {}, from: {}, template: {}, message: {}", mail.getReceiver(), MAIL_SENDER,
					mail.getTemplate(), e.getMessage(),e);
			throw new CustomResponseException(ResponseType.SERVER_ERROR);
		}

	}
}
