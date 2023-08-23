package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long> {

	@Query("select b from Banner b where b.id = :id")
	Optional<Banner> findById(@Param("id") Long id);

	@Query("select b from Banner b where b.activeYn = true and b.startDate <= now() and b.endDate >= now() order by b.sortOrder asc")
	List<Banner> findActive();

}
