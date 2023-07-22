package com.konnect.pet.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserWalkingHistory extends BaseAutoSetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "walking_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_walking_user"), nullable = false)
	private User user;

	private int meters;

	private int seconds;

	@Column(length = 2000)
	private String routes;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	@OneToMany(mappedBy = "userWalkingHistory", fetch = FetchType.LAZY)
	private List<UserWalkingRewardHistory> rewardHistories = new ArrayList<UserWalkingRewardHistory>();

	@OneToMany(mappedBy = "userWalkingHistory", fetch = FetchType.LAZY)
	private List<UserWalkingFootprintCatchHistory> footprintCatchHistories = new ArrayList<UserWalkingFootprintCatchHistory>();

	public UserWalkingHistory(User user, LocalDateTime startDate) {
		this.user = user;
		this.meters = 0;
		this.seconds = 0;
		this.routes = null;
		this.startDate = startDate;
		this.endDate = null;
	}

}
