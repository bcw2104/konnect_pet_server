package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserNotification;
import com.konnect.pet.entity.UserNotificationLog;

public interface UserNotificationLogRepository extends JpaRepository<UserNotificationLog,Long>{

	@Query("select n from UserNotificationLog n where n.id = :id")
	Optional<UserNotificationLog> findById(@Param("id") Long id);
	
	@Query("select n from UserNotificationLog n where n.user.id = :id")
	List<UserNotificationLog> findByUserId(@Param("userId") Long userId);
}
