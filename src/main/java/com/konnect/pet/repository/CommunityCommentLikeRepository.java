package com.konnect.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.CommunityCommentLike;
import com.konnect.pet.entity.CommunityPostLike;

public interface CommunityCommentLikeRepository extends JpaRepository<CommunityCommentLike, Long> {

	@Query("select count(c) from CommunityCommentLike c where c.user.id = :userId and c.comment.id = :commentId")
	int countByUserIdAndCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId);

	@Query("select c.comment.id from CommunityCommentLike c where c.user.id = :userId and c.comment.id in :commentIds")
	List<Long> findCommentIdsByUserIdAndCommentIds(@Param("userId") Long userId, @Param("commentIds") List<Long> commentIds);

}
