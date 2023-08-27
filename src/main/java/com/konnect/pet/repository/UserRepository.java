package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User u where u.id = :id")
	Optional<User> findById(@Param("id") Long id);

	@Query("select u from User u join fetch u.userPets up where u.id = :id")
	Optional<User> findWithPetsById(@Param("id") Long id);

	@Query("select u from User u where u.telHash = :telHash")
	Optional<User> findByTelHash(@Param("telHash") String telHash);

	@Query("select count(u) from User u join UserProfile up on u.id = up.user.id where u.status = :status")
	int countProfileHasUserByStatus(@Param("status") String status);

	boolean existsByEmail(@Param("email") String email);

	boolean existsByTelEnc(@Param("telEnc") String telEnc);

	boolean existsByTelHash(@Param("telHash") String telHash);

	Optional<User> findByEmail(@Param("email") String email);
}
