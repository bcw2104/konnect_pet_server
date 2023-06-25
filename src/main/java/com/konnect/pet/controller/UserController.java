package com.konnect.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.dto.UserSimpleDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	@GetMapping("/v1/info")
	public ResponseEntity<?> Test(Authentication authentication){
		User user = (User) authentication.getPrincipal();
		return ResponseEntity.ok(new ResponseDto(ResponseType.SUCCESS,new UserSimpleDto(user)));
	}

}
