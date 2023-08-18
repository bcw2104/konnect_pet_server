package com.konnect.pet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.dto.PageRequestDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.enums.code.PointTypeCode;
import com.konnect.pet.service.NotificationService;
import com.konnect.pet.service.PointService;
import com.konnect.pet.service.UserPetService;
import com.konnect.pet.service.UserService;

import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	private final NotificationService notificationService;
	private final UserService userService;
	private final UserPetService userPetService;
	private final PointService pointService;

	@GetMapping("/info")
	public ResponseEntity<?> userInfo(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userService.getUserSimplenfo(user));
	}

	@PatchMapping("/password")
	public ResponseEntity<?> password(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();
		String password = body.get("prev").toString();
		String newPassword = body.get("new").toString();

		return ResponseEntity.ok(userService.changePassword(user, password, newPassword));
	}

	@PostMapping("/device")
	public ResponseEntity<?> updateDeviceInfo(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(userService.updateDeviceInfo(user.getId(), body));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(userService.logout(user.getId()));
	}

	@GetMapping("/mypage")
	public ResponseEntity<?> mypage(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userService.getMypageData(user));
	}

	@PatchMapping("/mypage/marketing")
	public ResponseEntity<?> marketing(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();
		boolean marketingYn = Boolean.parseBoolean(body.get("marketingYn").toString());

		return ResponseEntity.ok(userService.changeMarketingYn(user.getId(), marketingYn));
	}

	@PostMapping("/mypage/leave")
	public ResponseEntity<?> leaveUser(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();
		String smsVerifyKey = body.get("smsVerifyKey").toString();

		return ResponseEntity.ok(userService.removeUser(user.getId(), smsVerifyKey));
	}

	@PostMapping("/verify/sms")
	public ResponseEntity<?> sendJoinVerifySms(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(userService.sendVerifyCodeBySms(user, LocationCode.LEAVE));
	}

	@PostMapping("/verify/sms/check")
	public ResponseEntity<?> checkJoinVerifySms(@RequestBody Map<String, Object> body) {
		Long reqId = Long.parseLong(body.get("reqId").toString());
		String tel = body.get("tel").toString();
		LocalDateTime timestamp = LocalDateTime.parse(body.get("timestamp").toString(),
				DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
		String verifyCode = body.get("verify").toString();

		return ResponseEntity.ok(userService.validateVerfiyCode(reqId, timestamp, verifyCode, tel, VerifyType.SMS));
	}

	@PostMapping("/profile")
	public ResponseEntity<?> saveProfileInfo(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userService.saveProfile(user, body));
	}

	@PutMapping("/pet")
	public ResponseEntity<?> savePetInfo(Authentication authentication, @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userPetService.saveOrEditPet(user, body, null));
	}

	@PatchMapping("/pet/{id}")
	public ResponseEntity<?> savePetInfo(Authentication authentication, @RequestBody Map<String, Object> body,
			@PathVariable("id") Long id) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userPetService.saveOrEditPet(user, body, id));
	}

	@DeleteMapping("/pet/{id}")
	public ResponseEntity<?> deletePetInfo(Authentication authentication, @PathVariable("id") Long id) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(userPetService.removePet(user, id));
	}

	@GetMapping("/noti")
	public ResponseEntity<?> notification(Authentication authentication, PageRequestDto pageDto) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(notificationService.getRecentUserNotifications(user, pageDto));
	}

	@GetMapping("/point/history")
	public ResponseEntity<?> pointHistory(Authentication authentication, @RequestParam("point") String pointType,
			@RequestParam("type") String type, PageRequestDto pageDto) {
		User user = (User) authentication.getPrincipal();
		return ResponseEntity
				.ok(pointService.getPointHistory(PointTypeCode.findByCode(pointType), type, pageDto, user));
	}

}
