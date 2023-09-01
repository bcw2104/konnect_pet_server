package com.konnect.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.CommunityCommentReportHistory;

public interface CommunityCommentReportHistoryRepository extends JpaRepository<CommunityCommentReportHistory, Long> {

	@Query("select count(c) from CommunityCommentReportHistory c where c.user.id = :userId and c.comment.id = :commentId")
	int countByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);
}
