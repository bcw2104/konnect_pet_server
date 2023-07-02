package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserRemoved;

public interface UserRemovedRepository extends JpaRepository<UserRemoved, Long>{

	@Query("select u from UserRemoved u where u.id = :id")
	Optional<UserRemoved> findById(@Param("id") Long id);

	@Query("select u from UserRemoved u where u.telHash = :telHash")
	Optional<UserRemoved> findByTelHash(@Param("telHash") String telHash);

	boolean existsByEmail(@Param("email") String email);
	boolean existsByTelEnc(@Param("telEnc") String telEnc);
	boolean existsByTelHash(@Param("telHash") String telHash);

	Optional<UserRemoved> findByEmail(@Param("email") String email);
}
