package com.konnect.pet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = { @UniqueConstraint(name = "UK_USER_POINT-USER_ID-POINT_TYPE", columnNames = { "user_id","pointType" })})
public class UserPoint extends BaseAutoSetEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "point_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_POINT-USER_ID"), nullable = false)
	private User user;

	private int balance;

	@Column(length = 3, nullable = false)
	private String pointType;
}
