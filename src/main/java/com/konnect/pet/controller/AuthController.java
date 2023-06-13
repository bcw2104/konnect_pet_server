package com.konnect.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;

	@GetMapping("/google/v1/userinfo")
	public ResponseEntity<?> getUserInfo(HttpServletRequest request){
		String token = request.getHeader("SOCIAL_AUTH_TOKEN").toString();

		return ResponseEntity.ok(userService.getSocialUserInfo(token, PlatformType.GOOGLE));
	}
}
