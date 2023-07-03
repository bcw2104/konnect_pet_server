package com.konnect.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.enums.code.LocationCode;
import com.konnect.pet.service.TermsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
public class TermsController {
	private final TermsService termsService;

	@GetMapping("/v1/group/signup")
	public ResponseEntity<?> signupTerms(){
		return ResponseEntity.ok(termsService.getTermsGroupByLocationCodeAndVisibleYn(LocationCode.SIGNUP,true));
	}
	
	@GetMapping("/v1/group/all")
	public ResponseEntity<?> allTerms(){
		return ResponseEntity.ok(termsService.getTermsGroupByLocationCodeAndVisibleYn(null,true));
	}

	@GetMapping("/v1/group/{groupId}/lastest")
	public ResponseEntity<?> termDetail(@PathVariable("groupId") Long termGroupId){
		return ResponseEntity.ok(termsService.getLastestTermsByTermsGroupId(termGroupId));
	}
}
