package com.konnect.pet.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.CommunityPost;

import jakarta.persistence.LockModeType;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

	@Query("select p from CommunityPost p where p.id = :id")
	Optional<CommunityPost> findById(@Param("id") Long id);

	@Query("select p from CommunityPost p where p.id = :id")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<CommunityPost> findByIdForUpdate(@Param("id") Long id);

	@Query("select p from CommunityPost p where p.id = :id and p.removedYn = false")
	Optional<CommunityPost> findActiveById(@Param("id") Long id);

	@Query("select count(p) from CommunityPost p where p.user.id = :userId and p.removedYn = false")
	int countActiveByUserId(@Param("userId") Long userId);

	@Modifying
	@Query("update CommunityPost p set p.content='*' where p.removedYn = true and p.removedDate < :beforeDate")
	int maskingRemovedByBeforeDate(@Param("beforeDate") LocalDateTime beforeDate);
}
