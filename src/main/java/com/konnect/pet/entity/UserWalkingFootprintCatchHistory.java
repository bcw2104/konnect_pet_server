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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserWalkingFootprintCatchHistory extends BaseAutoSetEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_WALKING_FOOTPRINT_CATCH_HISTORY-USER_ID"), nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "footprint_id", foreignKey = @ForeignKey(name = "FK_USER_WALKING_FOOTPRINT_CATCH_HISTORY-FOOTPRINT_ID"), nullable = false)
	private UserWalkingFootprint userWalkingFootprint;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "walking_id", foreignKey = @ForeignKey(name = "FK_USER_WALKING_FOOTPRINT_CATCH_HISTORY-WALKING_ID"), nullable = false)
	private UserWalkingHistory userWalkingHistory;

}
