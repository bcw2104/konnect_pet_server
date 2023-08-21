package com.konnect.pet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.konnect.pet.dto.FaqDto;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.repository.FaqRepository;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

	private final FaqRepository faqRepository;

	public ResponseDto getFaq() {
		List<FaqDto> faqs = faqRepository.findActiveAll().stream().map(FaqDto::new).toList();

		return new ResponseDto(ResponseType.SUCCESS, faqs);
	}
}
