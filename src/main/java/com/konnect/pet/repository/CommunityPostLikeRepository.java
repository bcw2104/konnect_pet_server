package com.konnect.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.CommunityPostLike;

public interface CommunityPostLikeRepository extends JpaRepository<CommunityPostLike, Long> {

	@Query("select count(c) from CommunityPostLike c where c.user.id = :userId and c.post.id = :postId")
	int countByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

	@Query("select c.post.id from CommunityPostLike c where c.user.id = :userId and c.post.id in :postIds")
	List<Long> findPostIdsByUserIdAndPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);

}
