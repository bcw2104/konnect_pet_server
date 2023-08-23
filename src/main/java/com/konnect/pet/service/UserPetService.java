package com.konnect.pet.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.konnect.pet.dto.UserPetDto;
import com.konnect.pet.entity.Properties;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserPet;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.PropertiesRepository;
import com.konnect.pet.repository.UserPetRepository;
import com.konnect.pet.response.ResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPetService {

	private final UserPetRepository userPetRepository;
	private final PropertiesRepository propertiesRepository;

	@Transactional
	public ResponseDto saveOrEditPet(User user, Map<String, Object> body, Long petId) {

		try {
			String petName = body.get("name").toString();
			String petType = body.get("type").toString();
			String petSpecies = body.get("species").toString();
			String petGender = body.get("gender").toString();
			BigDecimal petWeight = new BigDecimal(body.get("weight").toString());
			String birthDate = body.get("birthDate").toString();
			boolean neuteredYn = Boolean.parseBoolean(body.get("neuteredYn").toString());
			boolean inoculatedYn = Boolean.parseBoolean(body.get("inoculatedYn").toString());
			String petImgPath = body.get("imgPath") == null ? null : body.get("imgPath").toString();
			String petDescription = body.get("description") == null ? "" : body.get("description").toString();

			UserPet pet = null;
			if (petId != null) {
				pet = userPetRepository.findById(petId).orElse(null);

				if (pet == null || !pet.getUser().getId().equals(user.getId())) {
					throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
				}
				pet.setBirthDate(birthDate);
				pet.setInoculatedYn(inoculatedYn);
				pet.setNeuteredYn(neuteredYn);
				pet.setGender(petGender);
				pet.setWeight(petWeight);
				pet.setType(petType);
				pet.setName(petName);
				pet.setSpecies(petSpecies);
				pet.setDescription(petDescription);
				pet.setImgPath(petImgPath);

			} else {
				int petCount = userPetRepository.countByUserId(user.getId());

				int petMaxAddCount = Integer
						.parseInt(propertiesRepository.findValueByKey("pet_max_add_count").orElse("3"));

				if (petCount + 1 > petMaxAddCount) {
					return new ResponseDto(ResponseType.TOO_MANY_PET);
				}
				pet = new UserPet();
				pet.setUser(user);
				pet.setBirthDate(birthDate);
				pet.setInoculatedYn(inoculatedYn);
				pet.setNeuteredYn(neuteredYn);
				pet.setGender(petGender);
				pet.setWeight(petWeight);
				pet.setType(petType);
				pet.setName(petName);
				pet.setSpecies(petSpecies);
				pet.setDescription(petDescription);
				pet.setImgPath(petImgPath);

				userPetRepository.save(pet);
			}

			return new ResponseDto(ResponseType.SUCCESS, new UserPetDto(pet));

		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

	}

	@Transactional
	public ResponseDto removePet(User user, Long petId) {

		try {
			UserPet pet = null;
			pet = userPetRepository.findById(petId).orElse(null);

			if (pet == null || !pet.getUser().getId().equals(user.getId())) {
				throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
			}

			userPetRepository.delete(pet);

			return new ResponseDto(ResponseType.SUCCESS, new UserPetDto(pet));

		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

	}
}
