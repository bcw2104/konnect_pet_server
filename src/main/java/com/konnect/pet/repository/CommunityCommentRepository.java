package com.konnect.pet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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
}
