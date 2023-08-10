package com.konnect.pet.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserPointHistory;

public interface UserPointHistoryRepository extends JpaRepository<UserPointHistory, Long> {

	@Query("select ph from UserPointHistory ph where ph.id = :id")
	Optional<UserPointHistory> findById(@Param("id") Long id);

	@Query("select count(ph) from UserPointHistory ph where ph.user.id = :userId and ph.pointType = :pointType and ph.createdDate >= :afterDate")
	int countAfterDateByPointTypeAndUserId(@Param("userId") Long userId, @Param("pointType") String pointType,
			@Param("afterDate") LocalDateTime afterDate);

}
