package com.konnect.pet.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.dto.PageRequestDto;
import com.konnect.pet.entity.User;
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

	@PutMapping("/friend/{id}")
	public ResponseEntity<?> requestFriend(Authentication authentication, @PathVariable("id") Long toUserId) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(communityService.requestFriend(user, toUserId));
	}

	@GetMapping("/friend")
	public ResponseEntity<?> friends(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(communityService.getFriends(user));
	}

	@GetMapping("/friend/pending")
	public ResponseEntity<?> pendingFriends(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(communityService.getPendingFriends(user));
	}
	
	@PatchMapping("/friend/{id}")
	public ResponseEntity<?> replyFriend(Authentication authentication, @PathVariable("id") Long toUserId,
			@RequestBody Map<String,Object> body) {
		User user = (User) authentication.getPrincipal();
		
		ProcessStatusCode code = ProcessStatusCode.findByCode(body.get("code").toString());
		
		return ResponseEntity.ok(communityService.replyFriend(user,toUserId,code));
	}
}
