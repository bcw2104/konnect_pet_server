package com.konnect.pet.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnect.pet.annotation.DistributedLock;
import com.konnect.pet.dto.BannerDto;
import com.konnect.pet.dto.CommunityCategoryDto;
import com.konnect.pet.dto.CommunityCommentDto;
import com.konnect.pet.dto.CommunityPostDto;
import com.konnect.pet.dto.PageRequestDto;
import com.konnect.pet.dto.PickerItemDto;
import com.konnect.pet.dto.UserFriendDto;
import com.konnect.pet.dto.UserDetailDto;
import com.konnect.pet.entity.CommunityCategory;
import com.konnect.pet.entity.CommunityComment;
import com.konnect.pet.entity.CommunityCommentReportHistory;
import com.konnect.pet.entity.CommunityPost;
import com.konnect.pet.entity.CommunityPostFile;
import com.konnect.pet.entity.CommunityPostLike;
import com.konnect.pet.entity.CommunityPostReportHistory;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserFriend;
import com.konnect.pet.entity.UserProfile;
import com.konnect.pet.entity.UserWalkingFootprint;
import com.konnect.pet.enums.ReportType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.NotificationTypeCode;
import com.konnect.pet.enums.code.ProcessStatusCode;
import com.konnect.pet.enums.code.UserStatusCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.BannerRepository;
import com.konnect.pet.repository.CommunityCategoryRepository;
import com.konnect.pet.repository.CommunityCommentLikeRepository;
import com.konnect.pet.repository.CommunityCommentReportHistoryRepository;
import com.konnect.pet.repository.CommunityCommentRepository;
import com.konnect.pet.repository.CommunityPostFileRepository;
import com.konnect.pet.repository.CommunityPostLikeRepository;
import com.konnect.pet.repository.CommunityPostReportHistoryRepository;
import com.konnect.pet.repository.CommunityPostRepository;
import com.konnect.pet.repository.PropertiesRepository;
import com.konnect.pet.repository.UserFriendRepository;
import com.konnect.pet.repository.UserNotificationLogRepository;
import com.konnect.pet.repository.UserPetRepository;
import com.konnect.pet.repository.UserProfileRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.query.CommunityQueryRepository;
import com.konnect.pet.repository.query.UserFriendQueryRepository;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService {
	private final ObjectMapper objectMapper;

	private final UserRepository userRepository;
	private final UserPetRepository userPetRepository;
	private final UserFriendQueryRepository userFriendQueryRepository;
	private final UserProfileRepository userProfileRepository;
	private final UserFriendRepository userFriendRepository;
	private final BannerRepository bannerRepository;
	private final CommunityCategoryRepository communityCategoryRepository;
	private final CommunityPostRepository communityPostRepository;
	private final CommunityPostReportHistoryRepository communityPostReportHistoryRepository;
	private final CommunityPostLikeRepository communityPostLikeRepository;
	private final CommunityCommentRepository communityCommentRepository;
	private final CommunityCommentReportHistoryRepository communityCommentReportHistoryRepository;
	private final CommunityCommentLikeRepository communityCommentLikeRepository;
	private final CommunityPostFileRepository communityPostFileRepository;
	private final CommunityQueryRepository communityQueryRepository;
	private final PropertiesRepository propertiesRepository;
	private final S3StorageService s3StorageService;

	private final NotificationService notificationService;
	private final UserNotificationLogRepository userNotificationLogRepository;

	@Transactional
	public ResponseDto requestFriend(User user, Long toUserId) {
		User toUser = userRepository.findById(toUserId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		if (toUser.getStatus().equals(UserStatusCode.REMOVED.getCode())) {
			return new ResponseDto(ResponseType.LEAVED_USER);
		}

		UserFriend friend = userFriendRepository.findByFromUserIdAndToUserId(user.getId(), toUserId).orElse(null);
		UserFriend toFriend = userFriendRepository.findByFromUserIdAndToUserId(toUserId, user.getId()).orElse(null);

		if (toFriend != null && friend != null) {
			if (toFriend.getStatus().equals(ProcessStatusCode.PERMITTED.getCode())
					&& friend.getStatus().equals(ProcessStatusCode.PERMITTED.getCode())) {
				return new ResponseDto(ResponseType.SUCCESS, ProcessStatusCode.PERMITTED.getCode());
			} else if (toFriend.getStatus().equals(ProcessStatusCode.PENDING.getCode())) {
				friend.setStatus(ProcessStatusCode.PERMITTED.getCode());
				toFriend.setStatus(ProcessStatusCode.PERMITTED.getCode());
				notificationService.createMacroUserNotificationLog(toUser, NotificationTypeCode.ACCEPT_FRIEND);
				return new ResponseDto(ResponseType.SUCCESS, ProcessStatusCode.PERMITTED.getCode());
			} else {
				friend.setStatus(ProcessStatusCode.PENDING.getCode());
				toFriend.setStatus(ProcessStatusCode.NONE.getCode());
			}
		} else if (toFriend != null) {
			toFriend.setStatus(ProcessStatusCode.NONE.getCode());
			UserFriend newFriend = new UserFriend();
			newFriend.setFromUser(user);
			newFriend.setToUser(toUser);
			newFriend.setStatus(ProcessStatusCode.PENDING.getCode());
			userFriendRepository.save(newFriend);
		} else if (friend != null) {
			friend.setStatus(ProcessStatusCode.PENDING.getCode());

			UserFriend toNewFriend = new UserFriend();
			toNewFriend.setFromUser(toUser);
			toNewFriend.setToUser(user);
			toNewFriend.setStatus(ProcessStatusCode.NONE.getCode());
			userFriendRepository.save(toNewFriend);
		} else {
			UserFriend newFriend = new UserFriend();
			newFriend.setFromUser(user);
			newFriend.setToUser(toUser);
			newFriend.setStatus(ProcessStatusCode.PENDING.getCode());
			userFriendRepository.save(newFriend);

			UserFriend toNewFriend = new UserFriend();
			toNewFriend.setFromUser(toUser);
			toNewFriend.setToUser(user);
			toNewFriend.setStatus(ProcessStatusCode.NONE.getCode());
			userFriendRepository.save(toNewFriend);
		}
		notificationService.createMacroUserNotificationLog(toUser, NotificationTypeCode.REQUEST_FRIEND);

		return new ResponseDto(ResponseType.SUCCESS, ProcessStatusCode.PENDING.getCode());

	}

	@Transactional(readOnly = true)
	public ResponseDto getFriends(User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<UserFriendDto> friends = userFriendQueryRepository.findUserFriends(user.getId());

		resultMap.put("friends", friends);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional(readOnly = true)
	public ResponseDto getPendingFriends(User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<UserFriendDto> requestFriends = userFriendQueryRepository.findRequestFriends(user.getId());
		List<UserFriendDto> requestedFriends = userFriendQueryRepository.findRequestedFriends(user.getId());

		resultMap.put("request", requestFriends);
		resultMap.put("requested", requestedFriends);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional(readOnly = true)
	public ResponseDto getRecommendFriends(User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int userCount = userRepository.countProfileHasUserByStatus(UserStatusCode.ACTIVE.getCode());
		int limit = 10;

		Random random = new Random();

		random.setSeed(System.currentTimeMillis());
		int offset = random.nextInt(userCount - limit);
		List<UserFriendDto> recommendFriend = userFriendQueryRepository.findRecommendFriends(user.getId(), limit,
				offset);

		resultMap.put("recommends", recommendFriend);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional
	public ResponseDto replyFriend(User user, Long toUserId, ProcessStatusCode code) {
		if (!code.equals(ProcessStatusCode.PERMITTED) && !code.equals(ProcessStatusCode.CANCELED)
				&& !code.equals(ProcessStatusCode.DENIED)) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		User toUser = userRepository.findById(toUserId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		UserFriend friend = userFriendRepository.findByFromUserIdAndToUserId(user.getId(), toUserId).orElse(null);
		UserFriend toFriend = userFriendRepository.findByFromUserIdAndToUserId(toUserId, user.getId()).orElse(null);

		if (code.equals(ProcessStatusCode.CANCELED)) {
			if (friend == null || toFriend == null
					|| (!friend.getStatus().equals(ProcessStatusCode.PERMITTED.getCode())
							|| !toFriend.getStatus().equals(ProcessStatusCode.PERMITTED.getCode()))
							&& !friend.getStatus().equals(ProcessStatusCode.PENDING.getCode())
							&& !toFriend.getStatus().equals(ProcessStatusCode.PENDING.getCode())) {

				return new ResponseDto(ResponseType.DUPLICATED_REQUEST, code.getCode());
			}
		} else {
			if (friend == null || toFriend == null || (!friend.getStatus().equals(ProcessStatusCode.PENDING.getCode())
					&& !toFriend.getStatus().equals(ProcessStatusCode.PENDING.getCode()))) {
				return new ResponseDto(ResponseType.DUPLICATED_REQUEST, code.getCode());
			}
		}
		toFriend.setStatus(code.getCode());
		friend.setStatus(code.getCode());

		if (code.getCode().equals(ProcessStatusCode.PERMITTED.getCode())) {
			notificationService.createMacroUserNotificationLog(toUser, NotificationTypeCode.ACCEPT_FRIEND);
		}
		return new ResponseDto(ResponseType.SUCCESS, code.getCode());
	}

	@Transactional(readOnly = true)
	public ResponseDto getCommunityData(User user) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int newNotiCount = userNotificationLogRepository.countByUserIdAndVisitedYnIsFalse(user.getId());

		List<CommunityCategoryDto> categories = communityCategoryRepository.findActive().stream()
				.map(CommunityCategoryDto::new).toList();
		List<BannerDto> banners = bannerRepository.findActive().stream().map(BannerDto::new).toList();

		resultMap.put("categories", categories);
		resultMap.put("banners", banners);
		resultMap.put("newNotiCount", newNotiCount);
		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional(readOnly = true)
	public ResponseDto getUserDetail(User user, Long userId) {
		User targetUser = userRepository.findById(userId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		UserFriend friend = userFriendRepository.findByFromUserIdAndToUserId(user.getId(), targetUser.getId())
				.orElse(null);

		UserProfile profile = userProfileRepository.findByUserId(targetUser.getId()).orElse(new UserProfile());

		return new ResponseDto(ResponseType.SUCCESS, new UserDetailDto(targetUser, profile, friend));
	}

	@Transactional(readOnly = true)
	public ResponseDto getPosts(User user, Long categoryId, PageRequestDto pageDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int limit = pageDto.getSize() + 1;
		int offset = (pageDto.getPage() - 1) * pageDto.getSize();

		List<CommunityPostDto> posts = communityQueryRepository.findActivePosts(categoryId, limit, offset);

		boolean hasNext = false;
		if (posts.size() == limit) {
			posts.remove(limit - 1);
			hasNext = true;
		}
		List<Long> postIds = posts.stream().map(ele -> ele.getPostId()).toList();

		List<Long> likePostIds = communityPostLikeRepository.findPostIdsByUserIdAndPostIds(user.getId(), postIds);
		Map<Long, List<String>> filePaths = communityQueryRepository
				.findPostFilesByPostIds(postIds.toArray(Long[]::new));

		for (CommunityPostDto dto : posts) {
			if (dto.isRemovedYn() || dto.isBlockedYn()) {
				dto.setFilePaths(new ArrayList<>());
			} else {
				dto.setFilePaths(filePaths.getOrDefault(dto.getPostId(), new ArrayList<>()));
			}
			dto.setLikeYn(likePostIds.contains(dto.getPostId()));
		}
		resultMap.put("posts", posts);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional(readOnly = true)
	public ResponseDto getMyPosts(User user, PageRequestDto pageDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int limit = pageDto.getSize() + 1;
		int offset = (pageDto.getPage() - 1) * pageDto.getSize();

		List<CommunityPostDto> posts = communityQueryRepository.findActiveMyPosts(user.getId(), limit, offset);

		boolean hasNext = false;
		if (posts.size() == limit) {
			posts.remove(limit - 1);
			hasNext = true;
		}
		List<Long> postIds = posts.stream().map(ele -> ele.getPostId()).toList();

		List<Long> likePostIds = communityPostLikeRepository.findPostIdsByUserIdAndPostIds(user.getId(), postIds);
		Map<Long, List<String>> filePaths = communityQueryRepository
				.findPostFilesByPostIds(postIds.toArray(Long[]::new));

		for (CommunityPostDto dto : posts) {
			if (dto.isRemovedYn() || dto.isBlockedYn()) {
				dto.setFilePaths(new ArrayList<>());
			} else {
				dto.setFilePaths(filePaths.getOrDefault(dto.getPostId(), new ArrayList<>()));
			}
			dto.setLikeYn(likePostIds.contains(dto.getPostId()));
		}
		resultMap.put("posts", posts);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional
	public ResponseDto getPost(User user, Long postId) {
		CommunityPostDto post = communityQueryRepository.findPostById(postId);

		if (post == null) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		int likeYn = communityPostLikeRepository.countByUserIdAndPostId(user.getId(), postId);
		List<String> filePaths = (post.isRemovedYn() || post.isBlockedYn()) ? new ArrayList<>()
				: communityQueryRepository.findPostFilesByPostId(postId);

		post.setLikeYn(likeYn >= 1);
		post.setFilePaths(filePaths);

		return new ResponseDto(ResponseType.SUCCESS, post);
	}

	@Transactional(readOnly = true)
	public ResponseDto getPostFormData() {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int maxImageCount = Integer
				.parseInt(propertiesRepository.findValueByKey("community_post_max_image_count").orElse("6"));

		List<PickerItemDto> categories = communityCategoryRepository.findActive().stream()
				.map(ele -> new PickerItemDto(ele.getCategory(), String.valueOf(ele.getId()))).toList();

		resultMap.put("categories", categories);
		resultMap.put("maxImageCount", maxImageCount);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional
	public ResponseDto saveOrEditPost(User user, Map<String, Object> body, Long postId) {
		try {
			Long categoryId = Long.parseLong(body.get("category").toString());
			String content = body.get("content").toString();
			List<String> filePaths = objectMapper.readValue(body.get("imgPaths").toString(),
					new TypeReference<List<String>>() {
					});
			CommunityCategory category = communityCategoryRepository.findById(categoryId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			CommunityPost post = null;
			if (postId != null) {
				post = communityPostRepository.findById(postId).orElse(null);
				if (post == null || !post.getUser().getId().equals(user.getId())) {
					throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
				}

				post.setCategory(category);
				post.setContent(content);

				List<CommunityPostFile> files = post.getFiles();

				List<String> paths = files.stream().map(file -> file.getFilePath()).toList();
				s3StorageService.removeMultiOnS3(paths);

				communityPostFileRepository.deleteAllInBatch(files);
			} else {
				post = new CommunityPost();
				post.setCommentCount(0);
				post.setCategory(category);
				post.setContent(content);
				post.setUser(user);
				post.setRemovedYn(false);
				post.setLikeCount(0);

				communityPostRepository.save(post);
			}

			List<CommunityPostFile> files = new ArrayList<>();

			for (String path : filePaths) {
				CommunityPostFile file = new CommunityPostFile();
				file.setUser(user);
				file.setPost(post);
				file.setFilePath(path);

				files.add(file);
			}

			communityPostFileRepository.saveAll(files);

			return new ResponseDto(ResponseType.SUCCESS);
		} catch (Exception e) {
			log.error("Save post failed - user: {}, body: {}", user.getId(), body.toString(), e);
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER, e.getMessage());
		}
	}

	@Transactional
	public ResponseDto removePost(User user, Long postId) {

		CommunityPost post = communityPostRepository.findById(postId).orElse(null);
		if (post == null || !post.getUser().getId().equals(user.getId())) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		post.setRemovedYn(true);
		post.setRemovedDate(LocalDateTime.now());

		return new ResponseDto(ResponseType.SUCCESS);
	}

	@Transactional(readOnly = true)
	public ResponseDto getMyPostComments(User user, PageRequestDto pageDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int limit = pageDto.getSize() + 1;
		int offset = (pageDto.getPage() - 1) * pageDto.getSize();

		List<CommunityCommentDto> myComments = communityQueryRepository.findMyComments(user.getId(), limit, offset);

		boolean hasNext = false;
		if (myComments.size() == limit) {
			myComments.remove(limit - 1);
			hasNext = true;
		}

//		댓글 좋아요 기능 - 나중에 수요가 있을 시 추가
//		List<Long> childCommentIds = new ArrayList<>();
//
//		childComments.values().stream().forEach(ele->{
//			childCommentIds.addAll(ele.stream().map(ele2->ele2.getCommentId()).toList());
//		});
//
//		List<Long> commentIds = new ArrayList<>();
//		commentIds.addAll(parentCommentIds);
//		commentIds.addAll(childCommentIds);
//
//		List<Long> likeCommentIds = communityPostLikeRepository.findPostIdsByUserIdAndPostIds(user.getId(), commentIds);

		resultMap.put("comments", myComments);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional(readOnly = true)
	public ResponseDto getMyPostLikes(User user, PageRequestDto pageDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int limit = pageDto.getSize() + 1;
		int offset = (pageDto.getPage() - 1) * pageDto.getSize();

		List<CommunityPostDto> posts = communityQueryRepository.findActiveMyLikePosts(user.getId(), limit, offset);

		boolean hasNext = false;
		if (posts.size() == limit) {
			posts.remove(limit - 1);
			hasNext = true;
		}
		List<Long> postIds = posts.stream().map(ele -> ele.getPostId()).toList();

		Map<Long, List<String>> filePaths = communityQueryRepository
				.findPostFilesByPostIds(postIds.toArray(Long[]::new));

		for (CommunityPostDto dto : posts) {
			if (dto.isRemovedYn() || dto.isBlockedYn()) {
				dto.setFilePaths(new ArrayList<>());
			} else {
				dto.setFilePaths(filePaths.getOrDefault(dto.getPostId(), new ArrayList<>()));
			}
			dto.setLikeYn(true);
		}
		resultMap.put("posts", posts);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional(readOnly = true)
	public ResponseDto getPostComments(User user, Long postId, PageRequestDto pageDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int limit = pageDto.getSize() + 1;
		int offset = (pageDto.getPage() - 1) * pageDto.getSize();

		List<CommunityCommentDto> parentComments = communityQueryRepository.findParentComments(postId, limit, offset);

		boolean hasNext = false;
		if (parentComments.size() == limit) {
			parentComments.remove(limit - 1);
			hasNext = true;
		}
		List<Long> parentCommentIds = parentComments.stream().map(ele -> ele.getCommentId()).toList();

		Map<Long, List<CommunityCommentDto>> childComments = communityQueryRepository
				.findChildComments(parentCommentIds.toArray(Long[]::new));

//		댓글 좋아요 기능 - 나중에 수요가 있을 시 추가
//		List<Long> childCommentIds = new ArrayList<>();
//
//		childComments.values().stream().forEach(ele->{
//			childCommentIds.addAll(ele.stream().map(ele2->ele2.getCommentId()).toList());
//		});
//
//		List<Long> commentIds = new ArrayList<>();
//		commentIds.addAll(parentCommentIds);
//		commentIds.addAll(childCommentIds);
//
//		List<Long> likeCommentIds = communityPostLikeRepository.findPostIdsByUserIdAndPostIds(user.getId(), commentIds);

		for (CommunityCommentDto dto : parentComments) {
			List<CommunityCommentDto> children = childComments.get(dto.getCommentId());
			dto.setChildrens(children);

//			댓글 좋아요 기능 - 나중에 수요가 있을 시 추가
//			List<CommunityCommentDto> children = childComments.get(dto.getCommentId());
//
//			for (CommunityCommentDto child : children) {
//				child.setLikeYn(likeCommentIds.contains(dto.getCommentId()));
//			}
//
//			dto.setLikeYn(likeCommentIds.contains(dto.getCommentId()));
		}

		resultMap.put("comments", parentComments);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional
	public ResponseDto saveOrEditComment(User user, Map<String, Object> body, Long postId, Long commentId) {
		try {
			Long parentId = body.get("parentId") != null ? Long.parseLong(body.get("parentId").toString()) : null;
			String content = body.get("content").toString();
			String imagePath = body.get("imagePath") == null ? null : body.get("imagePath").toString();

			CommunityPost post = communityPostRepository.findByIdForUpdate(postId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			CommunityComment comment = null;
			if (commentId != null) {
				comment = communityCommentRepository.findById(commentId).orElse(null);
				if (comment == null || !comment.getUser().getId().equals(user.getId())
						|| !comment.getPost().getId().equals(postId)) {
					throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
				}

				s3StorageService.removeOnS3(comment.getImgPath());

				comment.setContent(content);
				comment.setImgPath(imagePath);
			} else {
				comment = new CommunityComment();
				comment.setContent(content);
				comment.setImgPath(imagePath);
				comment.setLikeCount(0);
				comment.setParentId(parentId);
				comment.setPost(post);
				comment.setRemovedYn(false);
				comment.setUser(user);

				communityCommentRepository.save(comment);

				post.setCommentCount(post.getCommentCount() + 1);
			}

			return new ResponseDto(ResponseType.SUCCESS);
		} catch (Exception e) {
			log.error("Save comment failed - user: {}, body: {}", user.getId(), body.toString(), e);
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER, e.getMessage());
		}
	}

	@Transactional
	public ResponseDto removeComment(User user, Long postId, Long commentId) {
		CommunityComment comment = communityCommentRepository.findById(commentId).orElse(null);
		if (comment == null || !comment.getUser().getId().equals(user.getId())
				|| !comment.getPost().getId().equals(postId)) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		comment.setRemovedYn(true);
		comment.setRemovedDate(LocalDateTime.now());

		return new ResponseDto(ResponseType.SUCCESS);
	}

	@DistributedLock(prefix = "POST_LIKE_", key = "#postId")
	@Transactional
	public ResponseDto changePostLike(User user, Long postId, boolean likeYn) {
		CommunityPostLike like = communityPostLikeRepository.findByUserIdAndPostId(user.getId(), postId).orElse(null);

		CommunityPost post = communityPostRepository.findById(postId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));
		if (likeYn) {
			if (like == null) {
				like = new CommunityPostLike();
				like.setPost(post);
				like.setUser(user);
				communityPostLikeRepository.save(like);
				post.setLikeCount(post.getLikeCount() + 1);
			}
		} else {
			if (like != null) {
				communityPostLikeRepository.delete(like);
				post.setLikeCount(post.getLikeCount() - 1);
			}
		}

		return new ResponseDto(ResponseType.SUCCESS);
	}

	@DistributedLock(prefix = "POST_LIKE_", key = "#postId")
	@Transactional
	public ResponseDto changePostLikeT(Long postId) {
		CommunityPost post = communityPostRepository.findById(postId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));
		post.setLikeCount(post.getLikeCount() + 1);
		return new ResponseDto(ResponseType.SUCCESS);
	}

	@DistributedLock(prefix = "REPORT_", key = "#type.name().concat('_').concat(#targetId)")
	@Transactional
	public ResponseDto report(User user, ReportType type, Long targetId) {
		if (type.equals(ReportType.POST)) {
			CommunityPost post = communityPostRepository.findById(targetId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			post.setReportCount(post.getReportCount() + 1);
			int reportCount = communityPostReportHistoryRepository.countByUserIdAndPostId(user.getId(), post.getId());

			if (reportCount > 0) {
				return new ResponseDto(ResponseType.ALREADY_REPORTED);
			}

			CommunityPostReportHistory reportHistory = new CommunityPostReportHistory();
			reportHistory.setPost(post);
			reportHistory.setUser(user);
			reportHistory.setReportType("999");
			communityPostReportHistoryRepository.save(reportHistory);

		} else if (type.equals(ReportType.COMMENT)) {
			CommunityComment comment = communityCommentRepository.findById(targetId)
					.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

			comment.setReportCount(comment.getReportCount() + 1);

			int reportCount = communityCommentReportHistoryRepository.countByUserIdAndCommentId(user.getId(),
					comment.getId());

			if (reportCount > 0) {
				return new ResponseDto(ResponseType.ALREADY_REPORTED);
			}

			CommunityCommentReportHistory reportHistory = new CommunityCommentReportHistory();
			reportHistory.setComment(comment);
			reportHistory.setUser(user);
			reportHistory.setReportType("999");
			communityCommentReportHistoryRepository.save(reportHistory);
		}
		return new ResponseDto(ResponseType.SUCCESS);
	}

}
