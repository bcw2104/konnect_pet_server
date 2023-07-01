package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserRemoved;

public interface UserRemovedRepository extends JpaRepository<UserRemoved, Long>{

	@Query("select u from UserRemoved u where u.id = :id")
	Optional<UserRemoved> findById(Long id);

	@Query("select u from UserRemoved u where u.telHash = :telHash")
	Optional<UserRemoved> findByTelHash(String telHash);

	boolean existsByEmail(String email);
	boolean existsByTelEnc(String telEnc);
	boolean existsByTelHash(String telHash);

	Optional<UserRemoved> findByEmail(String email);
}
