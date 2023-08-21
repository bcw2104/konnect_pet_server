package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.AppVersion;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

	@Query("select a from AppVersion a where a.id = :id")
	Optional<AppVersion> findById(@Param("id") Long id);

	@Query("select a from AppVersion a where a.version = :version")
	Optional<AppVersion> findByVersion(@Param("version") String version);

	@Query("select a from AppVersion a order by a.releasedDate desc")
	List<AppVersion> findTopOrderByReleasedDateDesc(Pageable pageable);

	@Query("select a from AppVersion a where a.forcedYn = true order by a.releasedDate desc")
	List<AppVersion> findTopByForcedYnOrderByReleasedDateDesc(@Param("forcedYn") boolean forcedYn, Pageable pageable);

}
