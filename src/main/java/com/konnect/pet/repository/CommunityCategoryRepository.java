package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.Banner;
import com.konnect.pet.entity.CommunityCategory;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {

	@Query("select c from CommunityCategory c where c.activeYn = true order by c.sortOrder asc")
	List<CommunityCategory> findActive();

}
