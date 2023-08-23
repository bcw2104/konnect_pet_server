package com.konnect.pet.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserWalkingRewardHistory extends BaseAutoSetEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reward_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_WALKING_REWARD_HISTORY-USER_ID"), nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_id", foreignKey = @ForeignKey(name = "FK_USER_WALKING_REWARD_HISTORY-POLICY_ID"), nullable = false)
	private WalkingRewardPolicy walkingRewardPolicy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "walking_id", foreignKey = @ForeignKey(name = "FK_USER_WALKING_REWARD_HISTORY-WALKING_ID"), nullable = false)
	private UserWalkingHistory userWalkingHistory;

	private int amount;

	private boolean paymentYn;

	@Column(length = 3, nullable = false)
	private String pointType;

	@Column(length = 200)
	private String policyName;

	@Column(length = 3, nullable = false)
	private String rewardProvideType;

	@Column(length = 3, nullable = false)
	private String rewardType;


}
