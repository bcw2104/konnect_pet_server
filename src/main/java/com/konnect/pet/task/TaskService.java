package com.konnect.pet.task;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.entity.CommunityPost;
import com.konnect.pet.repository.CommunityCommentLikeRepository;
import com.konnect.pet.repository.CommunityCommentRepository;
import com.konnect.pet.repository.CommunityPostFileRepository;
import com.konnect.pet.repository.CommunityPostLikeRepository;
import com.konnect.pet.repository.CommunityPostRepository;
import com.konnect.pet.repository.query.CommunityQueryRepository;
import com.konnect.pet.service.S3StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
	private final CommunityPostRepository communityPostRepository;
	private final CommunityPostLikeRepository communityPostLikeRepository;
	private final CommunityCommentRepository communityCommentRepository;
	private final CommunityCommentLikeRepository communityCommentLikeRepository;
	private final CommunityPostFileRepository communityPostFileRepository;
	private final CommunityQueryRepository communityQueryRepository;
	private final S3StorageService s3StorageService;

	@Transactional
	public void cleanRemovedPostAndComment() {
		LocalDateTime beforeDate = LocalDateTime.now().minusMonths(6).with(LocalTime.MIDNIGHT);

		List<String> postPaths = communityPostFileRepository.findRemovedFilePathByBeforeDate(beforeDate);
		List<String> commentPaths = communityCommentRepository.findRemovedFilePathByBeforeDate(beforeDate);

		int removedPosts = communityPostRepository.maskingRemovedByBeforeDate(beforeDate);
		int removedPostFiles = communityPostFileRepository.maskingRemovedByBeforeDate(beforeDate);
		int removedComments = communityCommentRepository.maskingRemovedByBeforeDate(beforeDate);

		if (postPaths.size() == removedPostFiles && commentPaths.size() == removedComments) {
			s3StorageService.removeMultiOnS3(postPaths);
			s3StorageService.removeMultiOnS3(commentPaths);
		}

		log.info("success to clean - post: {}, postFile: {}, comment: {}", removedPosts, removedPostFiles,
				removedComments);

	}
}
