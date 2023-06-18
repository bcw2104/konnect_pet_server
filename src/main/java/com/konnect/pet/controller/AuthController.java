package com.konnect.pet.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.service.AuthService;
import com.konnect.pet.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/v1/token/refresh")
	public ResponseEntity<?> tokenRefresh(HttpServletRequest request){

		return ResponseEntity.ok(authService.refreshToken(request));
	}

	@PostMapping("/v1/login")
	public ResponseEntity<?> login(@RequestBody Map<String, Object> body){
		String email = body.get("email").toString();
		String password = body.get("password").toString();


		return ResponseEntity.ok(authService.login(email, password, PlatformType.EMAIL));
	}

	@GetMapping("/v1/google/userinfo")
	public ResponseEntity<?> getUserInfo(HttpServletRequest request){
		String token = request.getHeader("SOCIAL_AUTH_TOKEN").toString();

		return ResponseEntity.ok(authService.getSocialUserInfo(token, PlatformType.GOOGLE));
	}
}
