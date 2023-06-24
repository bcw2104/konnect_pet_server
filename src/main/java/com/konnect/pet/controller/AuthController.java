package com.konnect.pet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.constant.CommonCodeConst;
import com.konnect.pet.dto.PickerItemDto;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.VerifyLocationCode;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.service.AuthService;
import com.konnect.pet.service.CommonCodeService;
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
	private final CommonCodeService commonCodeService;

	@GetMapping("/v1/screen/signup/step1")
	public ResponseEntity<?> signupStep1(){

		Map<String,Object> result = new HashMap<String, Object>();

		List<PickerItemDto> countryCodes = commonCodeService.getPickerItemByCodeGroup(CommonCodeConst.COUNTRY_CD);
		result.put("countryCodes",countryCodes);

		ResponseDto responseDto = new ResponseDto(ResponseType.SUCCESS,result);
		responseDto.setResult(result);

		return ResponseEntity.ok(responseDto);
	}

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

	@PostMapping("/v1/verify/sms")
	public ResponseEntity<?> sendVerifyms(@RequestBody Map<String, Object> body){
		String tel = body.get("tel").toString();

		return ResponseEntity.ok(authService.sendVerifyCodeBySms(tel, VerifyLocationCode.SIGNUP));
	}

	@PostMapping("/v1/verify/sms/check")
	public ResponseEntity<?> checkVerifyms(@RequestBody Map<String, Object> body){
		Long reqId = Long.parseLong(body.get("reqId").toString());
		LocalDateTime timestamp = LocalDateTime.parse(body.get("timestamp").toString(),DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
		String verifyCode = body.get("verify").toString();

		return ResponseEntity.ok(authService.validateVerfiyCode(reqId, timestamp, verifyCode, VerifyType.SMS));
	}

	@PostMapping("/v1/verify/email")
	public ResponseEntity<?> sendEmail(@RequestBody Map<String, Object> body){
		String email = body.get("email").toString();

		return ResponseEntity.ok(authService.sendVerifyCodeByEmail(email, VerifyLocationCode.SIGNUP));
	}
}
