package com.konnect.pet.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(uniqueConstraints = { @UniqueConstraint(name = "UK_USER_APP_SETTING-USER_ID", columnNames = { "user_id" }) })
public class UserAppSetting extends BaseAutoSetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "setting_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_APP_SETTING-USER_ID"), nullable = false)
	private User user;

	@ColumnDefault("true")
	private boolean walkingYn;

	@ColumnDefault("true")
	private boolean friendYn;

	@ColumnDefault("true")
	private boolean messageYn;

	@ColumnDefault("true")
	private boolean communityYn;

	@ColumnDefault("true")
	private boolean serviceYn;

	public void initSetting() {
		this.walkingYn = true;
		this.friendYn = true;
		this.messageYn = true;
		this.communityYn = true;
		this.serviceYn = true;
	}

}
