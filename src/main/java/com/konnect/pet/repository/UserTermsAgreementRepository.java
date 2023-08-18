package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserTermsAgreement;

public interface UserTermsAgreementRepository extends JpaRepository<UserTermsAgreement, Long> {

	Optional<UserTermsAgreement> findByUserIdAndTermsGroupId(@Param("userId") Long userId,
			@Param("termsGroupId") Long termsGroupId);
}
