package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.Terms;

public interface TermsRepository extends JpaRepository<Terms,Long>{

	@Query("select t from terms t where t.id = :id")
	Optional<Terms> findById(@Param("id") Long id);
}
