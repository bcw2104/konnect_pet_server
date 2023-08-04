package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserNotification;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

	@Query("select n from UserNotification n where n.id = :id")
	Optional<UserNotification> findById(@Param("id") Long id);

	@Query("select n from UserNotification n "
			+ "where n.notiType = :notiType and n.activeYn = true and n.startDate <= now() and n.endDate >= now()")
	List<UserNotification> findActiveByNotiType(@Param("notiType") String notiType);
}
