package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.konnect.pet.dto.FaqDto;
import com.konnect.pet.dto.PageRequestDto;
import com.konnect.pet.dto.PickerItemDto;
import com.konnect.pet.dto.QnaDto;
import com.konnect.pet.entity.Qna;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.ServiceCategoryCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.FaqRepository;
import com.konnect.pet.repository.PropertiesRepository;
import com.konnect.pet.repository.QnaRepository;
import com.konnect.pet.repository.query.CustomerQueryRepository;
import com.konnect.pet.repository.query.PropertiesQueryRepository;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

	private final FaqRepository faqRepository;
	private final QnaRepository qnaRepository;
	private final CustomerQueryRepository customerQueryRepository;
	private final PropertiesRepository propertiesRepository;

	public ResponseDto getFaq() {
		List<FaqDto> faqs = faqRepository.findActiveAll().stream().map(FaqDto::new).toList();

		return new ResponseDto(ResponseType.SUCCESS, faqs);
	}

	public ResponseDto getQnaFormData() {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("categories", ServiceCategoryCode.enumList.stream().map(PickerItemDto::new).toList());

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	public ResponseDto saveQna(User user, Map<String, Object> body) {
		Qna qna = new Qna();
		try {
			ServiceCategoryCode category = ServiceCategoryCode.findByCode(body.get("category").toString());
			String title = body.get("title").toString();
			String question = body.get("question").toString();

			if (category == null || StringUtils.isEmpty(title) || StringUtils.isEmpty(question)) {
				throw new CustomResponseException();
			}

			qna.setUser(user);
			qna.setQuestion(question);
			qna.setTitle(title);
			qna.setCategory(category.getCode());

		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		int maxCount = Integer.parseInt(propertiesRepository.findValueByKey("daily_max_qna_count").orElse("5"));
		int count = qnaRepository.countByUserIdAndAfterDate(user.getId(), LocalDateTime.now().with(LocalTime.MIDNIGHT));

		if (maxCount <= count) {
			return new ResponseDto(ResponseType.TOO_MANY_QNA_SUBMIT);
		}

		qnaRepository.save(qna);

		return new ResponseDto(ResponseType.SUCCESS);
	}

	public ResponseDto getQnas(User user, String type, PageRequestDto pageDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int limit = pageDto.getSize() + 1;
		int offset = (pageDto.getPage() - 1) * pageDto.getSize();

		List<QnaDto> qnas = customerQueryRepository.findQnas(user.getId(), type, limit, offset);

		boolean hasNext = false;
		if (qnas.size() == limit) {
			qnas.remove(limit - 1);
			hasNext = true;
		}
		resultMap.put("qnas", qnas);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	public ResponseDto getQnaDetail(User user, Long qnaId) {
		Qna qna = qnaRepository.findById(qnaId).orElse(null);

		if (qna == null || !qna.getUser().getId().equals(user.getId())) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		return new ResponseDto(ResponseType.SUCCESS, new QnaDto(qna));
	}
}
