package com.konnect.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.entity.User;
import com.konnect.pet.service.WalkingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/walking")
@RequiredArgsConstructor
public class WalkingContainer {

	private final WalkingService walkingService;


	@PostMapping("/v1/start")
	public ResponseEntity<?> startWalking(Authentication authentication){
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(walkingService.generateStartWalkingKey(user));
	}

}
