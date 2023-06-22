package com.konnect.pet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.konnect.pet.dto.PickerItemDto;
import com.konnect.pet.entity.CommonCode;
import com.konnect.pet.repository.CommonCodeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class CommonCodeService {
	private final CommonCodeRepository commonCodeRepository;

	public List<PickerItemDto> getPickerItemByCodeGroup(String codeGroup) {

		return commonCodeRepository.findByCodePair_CodeGroup(codeGroup).stream().map(ele -> new PickerItemDto(ele))
				.collect(Collectors.toList());
	}

	public List<CommonCode> getByCodeGroup(String codeGroup) {
		return commonCodeRepository.findByCodePair_CodeGroup(codeGroup);
	}
}
