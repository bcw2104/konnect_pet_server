package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.UserPointHistoryDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserPoint;
import com.konnect.pet.entity.UserPointHistory;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.PointHistoryTypeCode;
import com.konnect.pet.enums.code.PointTypeCode;
import com.konnect.pet.repository.UserPointHistoryRepository;
import com.konnect.pet.repository.UserPointRepository;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointService {

	private final UserPointRepository userPointRepository;
	private final UserPointHistoryRepository userPointHistoryRepository;

	@Transactional(readOnly = true)
	public ResponseDto getPointHistory(PointTypeCode pointType, User user) {
		LocalDateTime afterDate = LocalDateTime.now().minusMonths(5).with(LocalTime.MIDNIGHT);
		List<UserPointHistoryDto> pointHists = userPointHistoryRepository
				.findByPointTypeAndUserIdOrderByIdDesc(user.getId(), pointType.getCode(), afterDate).stream()
				.map(UserPointHistoryDto::new).toList();

		return new ResponseDto(ResponseType.SUCCESS, pointHists);
	}

	@Transactional
	public void createPointHistory(User user, PointTypeCode pointTypeCode, PointHistoryTypeCode historyTypeCode,
			int balance) {
		UserPointHistory history = new UserPointHistory();
		history.setBalance(balance);
		history.setHistoryType(historyTypeCode.getCode());
		history.setPointType(pointTypeCode.getCode());
		history.setUser(user);

		userPointHistoryRepository.save(history);
	}

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
