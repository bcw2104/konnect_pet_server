package com.konnect.pet.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MailDto {
	private String receiver;
	private String subject;
	private Map<String,Object> data;
	private String template;

}
