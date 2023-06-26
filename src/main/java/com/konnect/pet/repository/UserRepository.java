package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.konnect.pet.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	@Query("select u from User u where u.id = :id")
	Optional<User> findById(Long id);

	@Query("select u from User u where u.telHash = :telHash")
	Optional<User> findByTelHash(String telHash);

	boolean existsByEmail(String email);
	boolean existsByTelEnc(String telEnc);
	boolean existsByTelHash(String telHash);

	Optional<User> findByEmail(String email);
}
