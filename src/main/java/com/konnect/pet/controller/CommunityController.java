package com.konnect.pet.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.dto.PageRequestDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.ReportType;
import com.konnect.pet.enums.code.ProcessStatusCode;
import com.konnect.pet.service.CommonCodeService;
import com.konnect.pet.service.CommunityService;
import com.konnect.pet.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
public class CommunityController {

	private final UserService userService;
	private final CommunityService communityService;

	@GetMapping("")
	public ResponseEntity<?> community(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.getCommunityData(user));
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<?> userDetail(Authentication authentication, @PathVariable("id") Long userId) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.getUserDetail(user, userId));
	}

	@GetMapping("/posts")
	public ResponseEntity<?> posts(Authentication authentication,
			@RequestParam(name = "category", required = false) Long categoryId, PageRequestDto pageDto) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.getPosts(user, categoryId, pageDto));
	}

	@GetMapping("/posts/my")
	public ResponseEntity<?> myPosts(Authentication authentication, PageRequestDto pageDto) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.getMyPosts(user, pageDto));
	}

	@GetMapping("/posts/form")
	public ResponseEntity<?> postFormData() {
		return ResponseEntity.ok(communityService.getPostFormData());
	}

	@PostMapping("/posts")
	public ResponseEntity<?> savePost(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.saveOrEditPost(user, body, null));
	}

	@PutMapping("/posts/{id}")
	public ResponseEntity<?> editPost(Authentication authentication, @PathVariable("id") Long postId,
			@RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.saveOrEditPost(user, body, postId));
	}

	@DeleteMapping("/posts/{id}")
	public ResponseEntity<?> deletePost(Authentication authentication, @PathVariable("id") Long postId) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.removePost(user, postId));
	}

	@GetMapping("/posts/{id}")
	public ResponseEntity<?> post(Authentication authentication, @PathVariable("id") Long postId) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.getPost(user, postId));
	}

	@PostMapping("/posts/{id}/like")
	public ResponseEntity<?> changePostLike(Authentication authentication, @PathVariable("id") Long postId,
			@RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		boolean likeYn = Boolean.parseBoolean(body.get("likeYn").toString());
		return ResponseEntity.ok(communityService.changePostLike(user, postId, likeYn));
	}

	@GetMapping("/posts/like/my")
	public ResponseEntity<?> getMyLike(Authentication authentication, PageRequestDto pageDto) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.getMyPostLikes(user, pageDto));
	}

	@GetMapping("/comments/my")
	public ResponseEntity<?> myComment(Authentication authentication, PageRequestDto pageDto) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.getMyPostComments(user, pageDto));
	}

	@GetMapping("/posts/{id}/comments")
	public ResponseEntity<?> postComment(Authentication authentication, @PathVariable("id") Long postId,
			PageRequestDto pageDto) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.getPostComments(user, postId, pageDto));
	}

	@PostMapping("/posts/{id}/comments")
	public ResponseEntity<?> saveComment(Authentication authentication, @PathVariable("id") Long postId,
			@RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.saveOrEditComment(user, body, postId, null));
	}

	@PutMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<?> editComment(Authentication authentication, @PathVariable("postId") Long postId,
			@PathVariable("id") Long commentId, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.saveOrEditComment(user, body, postId, commentId));
	}

	@DeleteMapping("/posts/{postId}/comments/{id}")
	public ResponseEntity<?> deleteComment(Authentication authentication, @PathVariable("postId") Long postId,
			@PathVariable("id") Long commentId) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(communityService.removeComment(user, postId, commentId));
	}

	@PostMapping("/friends/{id}")
	public ResponseEntity<?> requestFriend(Authentication authentication, @PathVariable("id") Long toUserId) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(communityService.requestFriend(user, toUserId));
	}

	@GetMapping("/friends")
	public ResponseEntity<?> friends(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(communityService.getFriends(user));
	}

	@GetMapping("/friends/pending")
	public ResponseEntity<?> pendingFriends(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(communityService.getPendingFriends(user));
	}

	@GetMapping("/friends/recommend")
	public ResponseEntity<?> recommendFriends(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(communityService.getRecommendFriends(user));
	}

	@PatchMapping("/friends/{id}")
	public ResponseEntity<?> replyFriend(Authentication authentication, @PathVariable("id") Long toUserId,
			@RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		ProcessStatusCode code = ProcessStatusCode.findByCode(body.get("code").toString());

		return ResponseEntity.ok(communityService.replyFriend(user, toUserId, code));
	}

	@PostMapping("/report")
	public ResponseEntity<?> reportUser(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();
		ReportType type = ReportType.valueOf(body.get("type").toString());
		Long targetId = Long.parseLong(body.get("targetId").toString());

		return ResponseEntity.ok(communityService.report(user, type, targetId));
	}
}
