package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.TermsGroup;

public interface TermsGroupRepository extends JpaRepository<TermsGroup,Long>{

	@Query("select t from TermsGroup t where t.id = :id")
	Optional<TermsGroup> findById(@Param("id") Long id);

	List<TermsGroup> findByLocationCodeAndVisibleYnIsTrue(@Param("locationCode") String locationCode);
}
