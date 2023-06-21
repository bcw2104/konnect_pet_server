package com.konnect.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konnect.pet.entity.SmsVerifyLog;

public interface SmsVerifyLogRepository extends JpaRepository<SmsVerifyLog,Long>{

}
