package com.konnect.pet.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.CommunityComment;
import com.konnect.pet.entity.CommunityPost;

import jakarta.persistence.LockModeType;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

	@Query("select c from CommunityComment c where c.id = :id")
	Optional<CommunityComment> findById(@Param("id") Long id);

	@Query("select count(c) from CommunityComment c where c.user.id = :userId and c.removedYn = false")
	int countActiveByUserId(@Param("userId") Long userId);

	@Query("select c.imgPath from CommunityComment c where c.removedYn = true and c.removedDate < :beforeDate")
	List<String> findRemovedFilePathByBeforeDate(@Param("beforeDate") LocalDateTime beforeDate);
	
	@Modifying
	@Query("update CommunityComment c set c.content='*', c.imgPath='*' where c.removedYn = true and c.removedDate < :beforeDate")
	int maskingRemovedByBeforeDate(@Param("beforeDate") LocalDateTime beforeDate);
}
