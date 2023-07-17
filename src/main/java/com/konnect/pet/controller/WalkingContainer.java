package com.konnect.pet.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<?> startWalking(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(walkingService.generateStartWalkingData(user));
	}

	@PostMapping("/v1/save")
	public ResponseEntity<?> saveWalking(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(walkingService.saveWalking(user,body));
	}

	@GetMapping("/v1/report/{id}")
	public ResponseEntity<?> getWalkingReport(Authentication authentication,@PathVariable("id") Long id) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(walkingService.getWalkingHistory(user,id));
	}

}
