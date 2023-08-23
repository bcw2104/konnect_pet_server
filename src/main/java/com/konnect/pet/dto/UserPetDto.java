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

	private String name;

	private String type;
	private String typeName;

	private String species;

	private String gender;

	private BigDecimal weight;
	private String birthDate;

	private boolean neuteredYn;

	private boolean inoculatedYn;

	private String description;

	private String imgPath;

	public UserPetDto(UserPet pet) {
		this.petId = pet.getId();
		this.name = pet.getName();
		this.type = pet.getType();
		this.typeName = PetTypeCode.findByCode(pet.getType()).getCodeName();
		this.species = pet.getSpecies();
		this.weight = pet.getWeight();
		this.gender = pet.getGender();
		this.birthDate = pet.getBirthDate();
		this.neuteredYn = pet.isNeuteredYn();
		this.inoculatedYn = pet.isInoculatedYn();
		this.description = pet.getDescription();
		this.imgPath = pet.getImgPath();
	}


}
