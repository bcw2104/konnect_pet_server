package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import com.konnect.pet.dto.UserPetDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserPet;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserPetRepository;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPetService {

	private final UserPetRepository userPetRepository;

	public ResponseDto addNewPet(User user, Map<String, Object> body) {
		int petCount = userPetRepository.countByUserId(user.getId());

		if (petCount > 5) {
			return new ResponseDto(ResponseType.TOO_MANY_PET);
		}
		try {
			String petName = body.get("petName").toString();
			String petType = body.get("petType").toString();
			String petSpecies = body.get("petSpecies").toString();
			String petGender = body.get("petGender").toString();
			String birthDate = body.get("birthDate").toString();
			boolean neuteredYn = Boolean.parseBoolean(body.get("neuteredYn").toString());
			boolean inoculatedYn = Boolean.parseBoolean(body.get("inoculatedYn").toString());
			String petImgUrl = body.get("petImgUrl").toString();
			String petDescription = body.get("petDescription").toString();

			UserPet pet = new UserPet();
			pet.setUser(user);
			pet.setBirthDate(birthDate);
			pet.setInoculatedYn(inoculatedYn);
			pet.setNeuteredYn(neuteredYn);
			pet.setPetGender(petGender);
			pet.setPetType(petType);
			pet.setPetName(petName);
			pet.setPetSpecies(petSpecies);
			pet.setPetDescription(petDescription);
			pet.setPetImgUrl(petImgUrl);

			userPetRepository.save(pet);

			return new ResponseDto(ResponseType.SUCCESS, new UserPetDto(pet));

		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

	}
}
