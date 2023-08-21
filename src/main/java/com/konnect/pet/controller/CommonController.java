package com.konnect.pet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.constant.CommonCodeConst;
import com.konnect.pet.dto.PickerItemDto;
import com.konnect.pet.entity.AppVersion;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.response.ResponseDto;
import com.konnect.pet.service.AppService;
import com.konnect.pet.service.CommonCodeService;
import com.konnect.pet.service.UserService;
import com.konnect.pet.service.VerifyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common")
public class CommonController {
	private final CommonCodeService commonCodeService;
	private final AppService appService;

	@GetMapping("/public/nations")
	public ResponseEntity<?> screenSignupStep1(){

		Map<String,Object> result = new HashMap<String, Object>();

		List<PickerItemDto> nationCodes = commonCodeService.getPickerItemByCodeGroup(CommonCodeConst.COUNTRY_CD);
		result.put("nationCodes",nationCodes);

		return ResponseEntity.ok(new ResponseDto(ResponseType.SUCCESS,result));
	}

	@GetMapping("/public/app")
	public ResponseEntity<?> getAppInfo(@RequestParam("version") String version){
		return ResponseEntity.ok(appService.getAppInfo(version));
	}


}
