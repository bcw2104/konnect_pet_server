package com.konnect.pet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.dto.BannerDto;
import com.konnect.pet.dto.CommunityCategoryDto;
import com.konnect.pet.dto.CommunityCommentDto;
import com.konnect.pet.dto.CommunityPostDto;
import com.konnect.pet.dto.PageRequestDto;
import com.konnect.pet.dto.UserFriendDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserFriend;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.NotificationTypeCode;
import com.konnect.pet.enums.code.ProcessStatusCode;
import com.konnect.pet.enums.code.UserStatusCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.BannerRepository;
import com.konnect.pet.repository.CommunityCategoryRepository;
import com.konnect.pet.repository.CommunityCommentLikeRepository;
import com.konnect.pet.repository.CommunityPostFileRepository;
import com.konnect.pet.repository.CommunityPostLikeRepository;
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

	private final UserRepository userRepository;
	private final UserPetRepository userPetRepository;
	private final UserFriendQueryRepository userFriendQueryRepository;
	private final UserProfileRepository userProfileRepository;
	private final UserFriendRepository userFriendRepository;
	private final BannerRepository bannerRepository;
	private final CommunityCategoryRepository communityCategoryRepository;
	private final CommunityPostLikeRepository communityPostLikeRepository;
	private final CommunityCommentLikeRepository communityCommentLikeRepository;
	private final CommunityPostFileRepository communityPostFileRepository;
	private final CommunityQueryRepository communityQueryRepository;

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

	@Transactional
	public ResponseDto replyFriend(User user, Long toUserId, ProcessStatusCode code) {
		if (!code.equals(ProcessStatusCode.PERMITTED) && !code.equals(ProcessStatusCode.CANCELED)
				&& !code.equals(ProcessStatusCode.DENIED)) {
			throw new CustomResponseException(ResponseType.INVALID_PARAMETER);
		}

		User toUser = userRepository.findById(toUserId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		if (toUser.getStatus().equals(UserStatusCode.REMOVED.getCode())) {
			return new ResponseDto(ResponseType.LEAVED_USER);
		}

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
	public ResponseDto getPosts(User user, Long categoryId, PageRequestDto pageDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		int limit = pageDto.getSize() + 1;
		int offset = (pageDto.getPage() - 1) * pageDto.getSize();

		List<CommunityPostDto> posts = communityQueryRepository.findPosts(categoryId, limit, offset);

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
			dto.setFilePaths(filePaths.getOrDefault(dto.getPostId(), new ArrayList<>()));
			dto.setLikeYn(likePostIds.contains(dto.getPostId()));
		}
		resultMap.put("posts", posts);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	@Transactional
	public ResponseDto savePost(User user, Map<String, Object> map) {

		return new ResponseDto(ResponseType.SUCCESS);
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
		List<Long> commentIds = new ArrayList<>();
		List<Long> parentCommentIds = parentComments.stream().map(ele -> ele.getCommentId()).toList();

		Map<Long, List<CommunityCommentDto>> childComments = communityQueryRepository
				.findChildComments(parentCommentIds.toArray(Long[]::new));

		List<Long> childCommentIds = new ArrayList<>();
		
		childComments.values().stream().forEach(ele->{
			childCommentIds.addAll(ele.stream().map(ele2->ele2.getCommentId()).toList());
		});
				
		commentIds.addAll(parentCommentIds);
		commentIds.addAll(childCommentIds);
		
		List<Long> likeCommentIds = communityPostLikeRepository.findPostIdsByUserIdAndPostIds(user.getId(), commentIds);

		for (CommunityCommentDto dto : parentComments) {
			List<CommunityCommentDto> children = childComments.get(dto.getCommentId());

			for (CommunityCommentDto child : children) {
				child.setLikeYn(likeCommentIds.contains(dto.getCommentId()));
			}

			dto.setChildrens(children);
			dto.setLikeYn(likeCommentIds.contains(dto.getCommentId()));
		}

		resultMap.put("comments", parentComments);
		resultMap.put("hasNext", hasNext);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

}
