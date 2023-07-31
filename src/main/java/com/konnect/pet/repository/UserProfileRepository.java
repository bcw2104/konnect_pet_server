package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

	@Query("select p from UserProfile p where p.id = :id")
	Optional<UserProfile> findById(@Param("id") Long id);

	@Query("select p from UserProfile p where p.user.id = :userId")
	Optional<UserProfile> findByUserId(@Param("userId") Long userId);

}
