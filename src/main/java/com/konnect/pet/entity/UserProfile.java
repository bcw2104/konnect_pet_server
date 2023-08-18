package com.konnect.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class UserProfile extends BaseAutoSetEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_profile_user"), nullable = false)
	private User user;


	@Column(length = 30, nullable = false)
	private String nickname;

	@Column(length = 8, nullable = false)
	private String birthDate;

	@Column(length = 1, nullable = false)
	private String gender;

	@Column(length = 255)
	private String profileImgPath;

	@Column(length = 255)
	private String comment;

}
