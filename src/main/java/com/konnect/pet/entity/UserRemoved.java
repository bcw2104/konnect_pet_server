package com.konnect.pet.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(name = "UK_USER_ID", columnNames = { "userId" })})
public class UserRemoved extends BaseAutoSetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "removed_user_id")
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, nullable = false)
	private Roles role;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, nullable = false)
	private PlatformType platform;

	@Column(length = 50, nullable = false)
	private String email;

	@Column(length = 60, nullable = true)
	private String password;

	@Column(length = 5, nullable = false)
	private String nationCode;

	@Column(length = 20, nullable = false)
	private String telMask;

	@Column(length = 64, nullable = false)
	private String telHash;

	@Column(length = 64, nullable = false)
	private String telEnc;

	@Column(length = 10)
	private String deviceOs;

	@Column(length = 10)
	private String deviceOsVersion;

	@Column(length = 20)
	private String deviceModel;

	@Column(length = 200)
	private String deviceToken;

	public UserRemoved(User user) {
		this.userId = user.getId();
		this.role = user.getRole();
		this.platform = user.getPlatform();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.nationCode = user.getNationCode();
		this.telMask = user.getTelMask();
		this.telHash = user.getTelHash();
		this.telEnc = user.getTelEnc();
		this.deviceOs = user.getDeviceOs();
		this.deviceOsVersion = user.getDeviceOsVersion();
		this.deviceModel = user.getDeviceModel();
		this.deviceToken = user.getDeviceToken();
	}
}
