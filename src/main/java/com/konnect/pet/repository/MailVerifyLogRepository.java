package com.konnect.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konnect.pet.entity.MailVerifyLog;

public interface MailVerifyLogRepository extends JpaRepository<MailVerifyLog,Long>{

}
