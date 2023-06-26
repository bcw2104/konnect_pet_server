package com.konnect.pet.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.constant.CommonCodeConst;
import com.konnect.pet.dto.PickerItemDto;
import com.konnect.pet.dto.SignupRequestDto;
import com.konnect.pet.enums.PlatformType;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.VerifyType;
import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.service.AuthService;
import com.konnect.pet.service.CommonCodeService;
import com.konnect.pet.service.TermsService;
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
	private final TermsService termsService;

	@GetMapping("/v1/screen/signup/step1")
	public ResponseEntity<?> signupStep1(){

		Map<String,Object> result = new HashMap<String, Object>();

		List<PickerItemDto> nationCodes = commonCodeService.getPickerItemByCodeGroup(CommonCodeConst.COUNTRY_CD);
		result.put("nationCodes",nationCodes);

		ResponseDto responseDto = new ResponseDto(ResponseType.SUCCESS,result);
		responseDto.setResult(result);

		return ResponseEntity.ok(responseDto);
	}

	@GetMapping("/v1/screen/signup/step3")
	public ResponseEntity<?> signupStep3(){
		return ResponseEntity.ok(termsService.getTermsGroupByLocationCodeAndVisibleYn(LocationCode.SIGNUP,true));
	}

	@GetMapping("/v1/terms/group/{groupId}/lastest")
	public ResponseEntity<?> term(@PathVariable("groupId") Long termGroupId){
		return ResponseEntity.ok(termsService.getLastestTermsByTermsGroupId(termGroupId));
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

	@PostMapping("/v1/join")
	public ResponseEntity<?> join(@RequestBody SignupRequestDto requestDto){

		return ResponseEntity.ok(authService.join(requestDto));
	}

	@PostMapping("/v1/login/social")
	public ResponseEntity<?> googleLogin(@RequestBody Map<String, Object> body){
		String token = body.get("token").toString();
		PlatformType type = PlatformType.valueOf(body.get("type").toString());

		return ResponseEntity.ok(authService.socialLogin(token,type));
	}

	@PostMapping("/v1/verify/sms")
	public ResponseEntity<?> sendVerifySms(@RequestBody Map<String, Object> body){
		String tel = body.get("tel").toString();

		return ResponseEntity.ok(authService.sendVerifyCodeBySms(tel, LocationCode.SIGNUP));
	}

	@PostMapping("/v1/verify/sms/check")
	public ResponseEntity<?> checkVerifySms(@RequestBody Map<String, Object> body){
		Long reqId = Long.parseLong(body.get("reqId").toString());
		String encTel = body.get("encTel").toString();
		LocalDateTime timestamp = LocalDateTime.parse(body.get("timestamp").toString(),DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
		String verifyCode = body.get("verify").toString();

		authService.checkTelDuplication(encTel);

		return ResponseEntity.ok(authService.validateVerfiyCode(reqId, timestamp, verifyCode, VerifyType.SMS));
	}

	@PostMapping("/v1/verify/email")
	public ResponseEntity<?> sendEmail(@RequestBody Map<String, Object> body){
		String email = body.get("email").toString();

		return ResponseEntity.ok(authService.sendVerifyCodeByEmail(email, LocationCode.SIGNUP));
	}

	@PostMapping("/v1/verify/email/check")
	public ResponseEntity<?> checkVerifyEmail(@RequestBody Map<String, Object> body){
		Long reqId = Long.parseLong(body.get("reqId").toString());
		LocalDateTime timestamp = LocalDateTime.parse(body.get("timestamp").toString(),DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
		String email = body.get("email").toString();
		String verifyCode = body.get("verify").toString();

		authService.checkEmailDuplication(email);
		return ResponseEntity.ok(authService.validateVerfiyCode(reqId, timestamp, verifyCode, VerifyType.EMAIL));
	}
}
