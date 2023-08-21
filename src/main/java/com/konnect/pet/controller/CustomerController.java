package com.konnect.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
