package com.konnect.pet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.dto.UserSimpleDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	@GetMapping("/v1/info")
	public ResponseEntity<?> userInfo(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(new ResponseDto(ResponseType.SUCCESS, new UserSimpleDto(user)));
	}

	@PostMapping("/v1/device")
	public ResponseEntity<?> updateDeviceInfo(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(userService.updateDeviceInfo(user.getId(), body));
	}

	@PostMapping("/v1/mypage/leave")
	public ResponseEntity<?> leaveUser(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();
		String smsVerifyKey = body.get("smsVerifyKey").toString();

		return ResponseEntity.ok(userService.removeUser(user.getId(), smsVerifyKey));
	}

	@PostMapping("/v1/verify/sms")
	public ResponseEntity<?> sendJoinVerifySms(Authentication authentication){
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(userService.sendVerifyCodeBySms(user.getId(), LocationCode.LEAVE));
	}

	@PostMapping("/v1/verify/sms/check")
	public ResponseEntity<?> checkJoinVerifySms(@RequestBody Map<String, Object> body){
		Long reqId = Long.parseLong(body.get("reqId").toString());
		String tel = body.get("tel").toString();
		LocalDateTime timestamp = LocalDateTime.parse(body.get("timestamp").toString(),DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
		String verifyCode = body.get("verify").toString();

		return ResponseEntity.ok(userService.validateVerfiyCode(reqId, timestamp, verifyCode, tel,VerifyType.SMS));
	}

}
