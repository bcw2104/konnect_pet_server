package com.konnect.pet.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserDailyAccessLog;

public interface UserDailyAccessLogRepository extends JpaRepository<UserDailyAccessLog, Long> {

	@Query("select u from UserDailyAccessLog u where u.id = :id")
	Optional<UserDailyAccessLog> findById(@Param("id") Long id);
	
	@Query("select u from UserDailyAccessLog u where u.user.id = :userId and u.createdDate >= :afterDate")
	Optional<UserDailyAccessLog> findAfterByUserId(@Param("userId") Long userId,@Param("afterDate") LocalDateTime afterDate);
}
