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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UK_EMAIL", columnNames = { "email" }),
		@UniqueConstraint(name = "UK_TEL", columnNames = { "telHash" }) })
public class User extends BaseAutoSetEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

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

	@Column(length = 255)
	private String residenceAddress;

	@Column(length = 50)
	private String residenceCoords;

	@Column(length = 8)
	private String recommendCode;

	@Column(length = 200)
	private String deviceToken;

	private boolean marketingYn;

	private LocalDateTime lastLoginDate;

	@Column(length = 6)
	private String aktId;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(this.role);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


}
