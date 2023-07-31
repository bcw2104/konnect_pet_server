package com.konnect.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserProfile extends BaseAutoSetEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;


	@Column(length = 30, nullable = false)
	private String nickname;

	@Column(length = 8, nullable = false)
	private String birthDate;

	@Column(length = 1, nullable = false)
	private String gender;

	@Column(length = 255)
	private String profileImgUrl;

	@Column(length = 255)
	private String comment;

}