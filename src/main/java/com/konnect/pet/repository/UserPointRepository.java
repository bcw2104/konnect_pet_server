package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserPoint;

import jakarta.persistence.LockModeType;

public interface UserPointRepository extends JpaRepository<UserPoint, Long>{

	@Query("select u from User u where u.id = :id")
	Optional<UserPoint> findById(@Param("id") Long id);

	@Query("select u from UserPoint u where u.user.id = :userId")
	List<UserPoint> findByUserId(@Param("userId") Long userId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from UserPoint u where u.user.id = :userId")
	List<UserPoint> findByUserIdForUpdate(@Param("userId") Long userId);

	@Query("select u from UserPoint u where u.user.id = :userId and u.pointType = :pointType")
	Optional<UserPoint> findByUserIdAndPointType(@Param("userId") Long userId, @Param("pointType") String pointType);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from UserPoint u where u.user.id = :userId and u.pointType = :pointType")
	Optional<UserPoint> findByUserIdAndPointTypeForUpdate(@Param("userId") Long userId, @Param("pointType") String pointType);

}
