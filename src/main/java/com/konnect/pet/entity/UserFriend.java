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
@Setter
@Getter
@Table(uniqueConstraints = { @UniqueConstraint(name = "UK_USER_FRIEND-FROM_USER_ID-TO_USER_ID", columnNames = { "from_user_id","to_user_id" })})
public class UserFriend extends BaseAutoSetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "friend_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user_id", foreignKey = @ForeignKey(name = "FK_USER_FRIEND-FROM_USER_ID"), nullable = false)
	private User fromUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id", foreignKey = @ForeignKey(name = "FK_USER_FRIEND-TO_USER_ID"), nullable = false)
	private User toUser;

	@Column(length = 3, nullable = false)
	private String status;

}
