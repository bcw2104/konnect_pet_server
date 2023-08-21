package com.konnect.pet.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.konnect.pet.entity.User;
import com.konnect.pet.service.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service")
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping("/faq")
	public ResponseEntity<?> faq() {

		return ResponseEntity.ok(customerService.getFaq());
	}

	@GetMapping("/qna/new")
	public ResponseEntity<?> qnaForm() {

		return ResponseEntity.ok(customerService.getQnaFormData());
	}

	@PutMapping("/qna/new")
	public ResponseEntity<?> saveQna(Authentication authentication,  @RequestBody Map<String, Object> body) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(customerService.saveQna(user, body));
	}

	@GetMapping("/qna")
	public ResponseEntity<?> qna(Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		return ResponseEntity.ok(customerService.getQna(user.getId()));
	}
}
