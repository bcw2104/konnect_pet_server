package com.konnect.pet.entity;

import java.time.LocalDateTime;

import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.Roles;

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

@Setter
@Getter
@Entity
public class UserNotificationLog extends BaseAutoSetEntity{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="log_id")
	private Long id;
	
	private boolean visitedYn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "noti_id", foreignKey = @ForeignKey(name = "FK_noti_log_noti"), nullable = false)
	private UserNotification userNotification;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_noti_log_user"), nullable = false)
	private User user;

}
