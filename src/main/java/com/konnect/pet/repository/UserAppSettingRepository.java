package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserAppSetting;

import jakarta.persistence.LockModeType;

public interface UserAppSettingRepository extends JpaRepository<UserAppSetting, Long> {

	@Query("select u from UserAppSetting u where u.id = :id")
	Optional<UserAppSetting> findById(@Param("id") Long id);

	@Query("select u from UserAppSetting u where u.user.id = :userId")
	Optional<UserAppSetting> findByUserId(@Param("userId") Long userId);

	@Query("select u from UserAppSetting u where u.user.id = :userId")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<UserAppSetting> findByUserIdForUpdate(@Param("userId") Long userId);

}
