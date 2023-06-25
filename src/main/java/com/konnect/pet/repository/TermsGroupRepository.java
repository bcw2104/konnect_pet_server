package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.konnect.pet.entity.TermsGroup;

public interface TermsGroupRepository extends JpaRepository<TermsGroup,Long>{

	@Query("select t from TermsGroup t where t.id = :id")
	Optional<TermsGroup> findById(Long id);

	List<TermsGroup> findByLocationCodeAndVisibleYnIsTrue(String locationCode);
}
