package com.konnect.pet.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserPet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_pet_user"), nullable = false)
	private User user;

	@Column(length = 30, nullable = false)
	private String petName;

	@Column(length = 3, nullable = false)
	private String petType;

	@Column(length = 30)
	private String petSpecies;

	@Column(length = 1, nullable = false)
	private String petGender;

	@Column(precision = 10, scale = 2 ,nullable = false)
	private BigDecimal petWeight;

	@Column(length = 8, nullable = false)
	private String birthDate;

	private boolean neuteredYn;

	private boolean inoculatedYn;

	@Column(length = 255)
	private String petDescription;

	@Column(length = 255)
	private String petImgPath;


}
