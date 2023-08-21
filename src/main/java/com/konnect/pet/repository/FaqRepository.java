package com.konnect.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.konnect.pet.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Long> {

	@Query("select f from Faq f where f.activeYn = true order by f.sortOrder asc")
	List<Faq> findActiveAll();
}
