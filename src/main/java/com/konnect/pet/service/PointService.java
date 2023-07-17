package com.konnect.pet.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserPoint;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.PointTypeCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserPointRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointService {

	private final UserPointRepository userPointRepository;

	@Transactional
	public void increaseUserPoint(User user, PointTypeCode pointTypeCode, int balance) {
		Optional<UserPoint> userPointOpt = userPointRepository.findByUserIdAndPointTypeForUpdate(user.getId(),
				pointTypeCode.getCode());

		if (userPointOpt.isPresent()) {
			UserPoint userPoint = userPointOpt.get();

			userPoint.setBalance(userPoint.getBalance() + balance);
		} else {
			UserPoint userPoint = new UserPoint();
			userPoint.setPointType(pointTypeCode.getCode());
			userPoint.setUser(user);
			userPoint.setBalance(balance);

			userPointRepository.save(userPoint);
		}
	}

	@Transactional
	public void decreaseUserPoint(User user, PointTypeCode pointTypeCode, int balance) throws Exception {
		Optional<UserPoint> userPointOpt = userPointRepository.findByUserIdAndPointTypeForUpdate(user.getId(),
				pointTypeCode.getCode());

		if (userPointOpt.isPresent()) {
			UserPoint userPoint = userPointOpt.get();
			if (userPoint.getBalance() < balance) {

				throw new Exception("not enough point");
			}
			userPoint.setBalance(userPoint.getBalance() - balance);
		} else {
			throw new Exception("not enough point");
		}
	}

}
