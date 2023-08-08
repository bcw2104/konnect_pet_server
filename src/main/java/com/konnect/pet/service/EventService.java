package com.konnect.pet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.entity.EventRewardPolicy;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.code.PointHistoryTypeCode;
import com.konnect.pet.enums.code.PointTypeCode;
import com.konnect.pet.repository.EventRewardPolicyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
	private final PointService pointService;
	private final EventRewardPolicyRepository eventRewardPolicyRepository;

	@Transactional
	public void provideEventReward(User user, PointHistoryTypeCode typeCode) {
		List<EventRewardPolicy> rewardPolicies = eventRewardPolicyRepository
				.findActiveByHistoryType(typeCode.getCode());

		for (EventRewardPolicy policy : rewardPolicies) {
			try {
				log.info("provide event reward - user: {}, eventId: {}", user.getId(), policy.getId());
				PointTypeCode pointTypeCode = PointTypeCode.findByCode(policy.getPointType());
				pointService.increaseUserPoint(user, pointTypeCode, policy.getBalance());
				pointService.createPointHistory(user, pointTypeCode, typeCode, policy.getBalance());
			} catch (Exception e) {
				log.error("failed to provide event reward - user: {}, eventId: {}", user.getId(), policy.getId());
			}
		}
	}

}
