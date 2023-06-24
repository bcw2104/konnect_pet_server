package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.konnect.pet.entity.MailVerifyLog;
import com.konnect.pet.entity.User;

public interface MailVerifyLogRepository extends JpaRepository<MailVerifyLog,Long>{

	@Query("select m from MailVerifyLog m where m.id = :id")
	Optional<MailVerifyLog> findById(Long id);
}
