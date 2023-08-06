package com.konnect.pet.dto;

import java.math.BigDecimal;

import com.konnect.pet.entity.UserPet;
import com.konnect.pet.enums.code.PetTypeCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPetDto {

	private Long petId;

	private String petName;

	private String petType;
	private String petTypeName;

	private String petSpecies;

	private String petGender;

	private BigDecimal petWeight;
	private String birthDate;

	private boolean neuteredYn;

	private boolean inoculatedYn;

	private String petDescription;

	private String petImgUrl;

	public UserPetDto(UserPet pet) {
		this.petId = pet.getId();
		this.petName = pet.getPetName();
		this.petType = pet.getPetType();
		this.petTypeName = PetTypeCode.findByCode(pet.getPetType()).getCodeName();
		this.petSpecies = pet.getPetSpecies();
		this.petWeight = pet.getPetWeight();
		this.petGender = pet.getPetGender();
		this.birthDate = pet.getBirthDate();
		this.neuteredYn = pet.isNeuteredYn();
		this.inoculatedYn = pet.isInoculatedYn();
		this.petDescription = pet.getPetDescription();
		this.petImgUrl = pet.getPetImgUrl();
	}


}
