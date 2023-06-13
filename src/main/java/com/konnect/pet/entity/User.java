package com.konnect.pet.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(uniqueConstraints = { @UniqueConstraint(name = "UK_EMAIL", columnNames = { "email" }),
		@UniqueConstraint(name = "UK_TEL", columnNames = { "telHash" }) })
public class User extends BaseAutoSetEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(length = 10, nullable = false)
	private String role;

	@Column(length = 50, unique = true, nullable = false)
	private String email;

	@Column(length = 60, nullable = false)
	private String password;

	@Column(length = 64, nullable = false)
	private String telHash;

	@Column(length = 64, nullable = false)
	private String telEnc;

	@Column(length = 6)
	private String authTokenId;

	@Column(length = 10)
	private String deviceOs;

	@Column(length = 20)
	private String deviceName;

	@Column(length = 200, nullable = false)
	private String deviceToken;

	private boolean marketingYn;

	// TODO 주소 추가

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return role;
			}
		});
		return collect;
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
