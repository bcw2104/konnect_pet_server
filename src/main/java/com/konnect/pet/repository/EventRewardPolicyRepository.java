package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.EventRewardPolicy;
import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserNotification;

public interface EventRewardPolicyRepository extends JpaRepository<EventRewardPolicy, Long> {

	@Query("select e from EventRewardPolicy e where e.id = :id")
	Optional<EventRewardPolicy> findById(@Param("id") Long id);

	@Query("select e from EventRewardPolicy e where e.historyType = :historyType and e.activeYn = true and e.startDate <= now() and e.endDate >= now()")
	List<EventRewardPolicy> findActiveByHistoryType(@Param("historyType") String historyType);
}
