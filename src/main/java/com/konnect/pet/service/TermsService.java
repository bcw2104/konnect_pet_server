package com.konnect.pet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.konnect.pet.dto.PickerItemDto;
import com.konnect.pet.dto.TermsDto;
import com.konnect.pet.entity.CommonCode;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.repository.CommonCodeRepository;
import com.konnect.pet.repository.TermsGroupRepository;
import com.konnect.pet.repository.TermsRepository;
import com.konnect.pet.repository.query.TermsQueryRepository;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class TermsService {
	private final TermsRepository termsRepository;
	private final TermsQueryRepository termsQueryRepository;
	private final TermsGroupRepository termsGroupRepository;

	public ResponseDto getLastestTermsByTermsGroupId(Long termsGroupId) {
		return new ResponseDto(ResponseType.SUCCESS, termsQueryRepository.findLastestTermsByTermsGroupsId(termsGroupId));
	}

	public ResponseDto getTermsGroupByLocationCodeAndVisibleYn(LocationCode locationCode, boolean visibleYn) {
		return new ResponseDto(ResponseType.SUCCESS, termsQueryRepository.findTermsGroupByLocationCodeAndVisibleYn(locationCode,visibleYn));
	}
}
