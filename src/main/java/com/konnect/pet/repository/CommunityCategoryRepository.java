package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.Banner;
import com.konnect.pet.entity.CommunityCategory;
import com.konnect.pet.entity.CommunityPost;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {

	@Query("select c from CommunityCategory c where c.id = :id")
	Optional<CommunityCategory> findById(@Param("id") Long id);
	
	@Query("select c from CommunityCategory c where c.activeYn = true order by c.sortOrder asc")
	List<CommunityCategory> findActive();

}
