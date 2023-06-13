package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.konnect.pet.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> getByEmail(String email);
}
