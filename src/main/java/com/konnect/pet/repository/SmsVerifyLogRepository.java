package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.SmsVerifyLog;

public interface SmsVerifyLogRepository extends JpaRepository<SmsVerifyLog,Long>{

	@Query("select s from SmsVerifyLog s where s.id = :id")
	Optional<SmsVerifyLog> findById(Long id);
}
