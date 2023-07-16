package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.WalkingRewardPolicy;

public interface WalkingRewardPolicyRepository extends JpaRepository<WalkingRewardPolicy, Long> {

	@Query("select u from User u where u.id = :id")
	Optional<WalkingRewardPolicy> findById(@Param("id") Long id);

	@Query("select w from WalkingRewardPolicy w")
	List<WalkingRewardPolicy> findAll();

	@Query("select w from WalkingRewardPolicy w where w.activeYn = true and w.startDate <= now() and w.endDate >= now() order by w.sortOrder asc")
	List<WalkingRewardPolicy> findActiveAll();
}
