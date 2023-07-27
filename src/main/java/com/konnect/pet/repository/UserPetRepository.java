package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserPet;

public interface UserPetRepository extends JpaRepository<UserPet, Long>{

	@Query("select u from UserPet u where u.id = :id")
	Optional<UserPet> findById(@Param("id") Long id);

	@Query("select u from UserPet u where u.user.id = :userId")
	List<UserPet> findByUserId(@Param("userId") Long userId);

	@Query("select count(u) from UserPet u where u.user.id = :userId")
	int countByUserId(@Param("userId") Long userId);

}
