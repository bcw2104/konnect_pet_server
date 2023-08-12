package com.konnect.pet.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.entity.User;
import com.konnect.pet.service.WalkingService;

import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/walking")
@RequiredArgsConstructor
public class WalkingContainer {

	private final WalkingService walkingService;

	@PostMapping("/start")
	public ResponseEntity<?> startWalking(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(walkingService.generateStartWalkingData(user));
	}

	@PostMapping("/save")
	public ResponseEntity<?> saveWalking(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(walkingService.saveWalking(user, body));
	}

	@GetMapping("/report/{id}")
	public ResponseEntity<?> getWalkingReport(Authentication authentication, @PathVariable("id") Long id) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(walkingService.getWalkingHistory(user, id));
	}

	@GetMapping("/footprints/around")
	public ResponseEntity<?> getAroundFootprints(Authentication authentication, @RequestParam("lat") double lat,
			@RequestParam("lng") double lng) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(walkingService.getAroundFootprint(user, lat, lng));
	}

	@GetMapping("/footprints/{id}")
	public ResponseEntity<?> getFootprintInfo(Authentication authentication, @PathVariable("id") Long footprintId) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(walkingService.getFootprintInfo(user, footprintId));
	}

	@GetMapping("/summary")
	public ResponseEntity<?> getWalkingSummary(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(walkingService.getWalkingSummary(user));
	}

	@GetMapping("/history")
	public ResponseEntity<?> getWalkingHistory(Authentication authentication,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(walkingService.getWalkingHistories(user, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate)));
	}

}
