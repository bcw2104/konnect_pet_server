package com.konnect.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.entity.User;
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

}
