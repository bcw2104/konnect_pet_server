package com.konnect.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.CommunityPostReportHistory;

public interface CommunityPostReportHistoryRepository extends JpaRepository<CommunityPostReportHistory, Long> {
	@Query("select count(c) from CommunityPostReportHistory c where c.user.id = :userId and c.post.id = :postId")
	int countByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
}
