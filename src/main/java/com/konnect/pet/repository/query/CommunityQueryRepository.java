package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QCommunityCategory.communityCategory;
import static com.konnect.pet.entity.QCommunityComment.communityComment;
import static com.konnect.pet.entity.QCommunityPost.communityPost;
import static com.konnect.pet.entity.QCommunityPostFile.communityPostFile;
import static com.konnect.pet.entity.QUser.user;
import static com.konnect.pet.entity.QUserProfile.userProfile;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.CommunityCommentDto;
import com.konnect.pet.dto.CommunityPostDto;
import com.konnect.pet.entity.QCommunityComment;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommunityQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<CommunityPostDto> findPosts(Long categoryId, int limit, int offset) {
		return queryFactory
				.select(Projections.constructor(CommunityPostDto.class, communityPost.id, communityPost.category.id,
						communityPost.category.category, communityPost.user.id, userProfile.nickname,
						userProfile.imgPath, communityPost.content, communityPost.likeCount, communityPost.commentCount,
						communityPost.createdDate))
				.from(communityPost).join(communityPost.category, communityCategory).join(userProfile)
				.on(communityPost.user.id.eq(userProfile.user.id))
				.where((categoryId.equals(-1L) ? null : communityPost.category.id.eq(categoryId))).limit(limit)
				.offset(offset).orderBy(communityPost.id.desc()).fetch();
	}

	public List<CommunityCommentDto> findParentComments(Long postId, int limit, int offset) {
		return queryFactory
				.select(Projections.constructor(CommunityCommentDto.class, communityComment.id,
						communityComment.post.id, communityComment.user.id, userProfile.nickname, userProfile.imgPath,
						communityComment.content, communityComment.likeCount, communityPost.createdDate,
						communityComment.imgPath, communityComment.parentId, communityComment.removedYn))
				.from(communityComment).join(userProfile).on(communityComment.user.id.eq(userProfile.user.id))
				.where(communityComment.post.id.eq(postId), communityComment.parentId.isNull()).limit(limit)
				.offset(offset).orderBy(communityComment.id.asc()).fetch();
	}

	public Map<Long, List<CommunityCommentDto>> findChildComments(Long[] parentIds) {
		return queryFactory.from(communityComment).join(userProfile)
				.on(communityComment.user.id.eq(userProfile.user.id)).where(communityComment.parentId.in(parentIds))
				.transform(GroupBy.groupBy(communityComment.parentId)
						.as(GroupBy.list(Projections.constructor(CommunityCommentDto.class, communityComment.id,
								communityComment.post.id, communityComment.user.id, userProfile.nickname,
								userProfile.imgPath, communityComment.content, communityComment.likeCount,
								communityPost.createdDate, communityComment.imgPath, communityComment.parentId,
								communityComment.removedYn))));
	}

	public Map<Long, List<String>> findPostFilesByPostIds(Long[] postIds) {
		return queryFactory.from(communityPostFile).where(communityPostFile.post.id.in(postIds))
				.transform(GroupBy.groupBy(communityPostFile.post.id).as(GroupBy.list(communityPostFile.filePath)));
	}
}
