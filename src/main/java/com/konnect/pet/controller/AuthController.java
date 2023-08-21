package com.konnect.pet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.dto.AuthRequestDto;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.service.AuthService;
import com.konnect.pet.service.VerifyService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;
	private final VerifyService verifyService;

	@PostMapping("/token/refresh")
	public ResponseEntity<?> tokenRefresh(HttpServletRequest request){

		return ResponseEntity.ok(authService.refreshToken(request));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, Object> body){
		String email = body.get("email").toString();
		String password = body.get("password").toString();

		return ResponseEntity.ok(authService.login(email, password, PlatformType.EMAIL));
	}

	@PostMapping("/login/social")
	public ResponseEntity<?> googleLogin(@RequestBody Map<String, Object> body){
		String token = body.get("token").toString();
		PlatformType type = PlatformType.valueOf(body.get("type").toString());

		return ResponseEntity.ok(authService.socialLogin(token,type));
	}

	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody AuthRequestDto requestDto){

		return ResponseEntity.ok(authService.join(requestDto));
	}

	@PostMapping("/join/verify/sms")
	public ResponseEntity<?> sendJoinVerifySms(@RequestBody Map<String, Object> body){
		String tel = body.get("tel").toString();

		return ResponseEntity.ok(verifyService.sendVerifyCodeBySms(tel, LocationCode.SIGNUP));
	}

	@PostMapping("/join/verify/sms/check")
	public ResponseEntity<?> checkJoinVerifySms(@RequestBody Map<String, Object> body){
		Long reqId = Long.parseLong(body.get("reqId").toString());
		String tel = body.get("tel").toString();
		LocalDateTime timestamp = LocalDateTime.parse(body.get("timestamp").toString(),DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
		String verifyCode = body.get("verify").toString();
		authService.checkTelBeforeVerify(tel, true);

		return ResponseEntity.ok(verifyService.validateVerfiyCode(reqId, timestamp, verifyCode, tel,VerifyType.SMS));
	}

	@PostMapping("/join/verify/email")
	public ResponseEntity<?> sendJoinVerifyEmail(@RequestBody Map<String, Object> body){
		String email = body.get("email").toString();
		authService.checkEmailBeforeVerify(email,true);

		return ResponseEntity.ok(verifyService.sendVerifyCodeByEmail(email, LocationCode.SIGNUP));
	}

	@PostMapping("/join/verify/email/check")
	public ResponseEntity<?> checkJoinVerifyEmail(@RequestBody Map<String, Object> body){
		Long reqId = Long.parseLong(body.get("reqId").toString());
		LocalDateTime timestamp = LocalDateTime.parse(body.get("timestamp").toString(),DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
		String email = body.get("email").toString();
		String verifyCode = body.get("verify").toString();

		return ResponseEntity.ok(verifyService.validateVerfiyCode(reqId, timestamp, verifyCode,email, VerifyType.EMAIL));
	}

	@PostMapping("/password/reset")
	public ResponseEntity<?> resetPassword(@RequestBody AuthRequestDto requestDto){

		return ResponseEntity.ok(authService.resetPassword(requestDto));
	}

	@PostMapping("/password/reset/verify/email")
	public ResponseEntity<?> commonVerifyEmail(@RequestBody Map<String, Object> body){
		String email = body.get("email").toString();
		authService.checkEmailBeforeVerify(email,false);

		return ResponseEntity.ok(verifyService.sendVerifyCodeByEmail(email, LocationCode.PASSWORD_RESET));
	}

	@PostMapping("/password/reset/verify/email/check")
	public ResponseEntity<?> checkVerifyEmail(@RequestBody Map<String, Object> body){
		Long reqId = Long.parseLong(body.get("reqId").toString());
		LocalDateTime timestamp = LocalDateTime.parse(body.get("timestamp").toString(),DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
		String email = body.get("email").toString();
		String verifyCode = body.get("verify").toString();

		return ResponseEntity.ok(verifyService.validateVerfiyCode(reqId, timestamp, verifyCode,email, VerifyType.EMAIL));
	}


}
