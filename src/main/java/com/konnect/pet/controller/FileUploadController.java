package com.konnect.pet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.konnect.pet.constant.ServiceConst;
import com.konnect.pet.service.S3StorageService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class FileUploadController {

	private final S3StorageService s3StorageService;

	@PostMapping("/images/profile/user")
	public ResponseEntity<?> uploadUserImage(@RequestParam("image") MultipartFile multipartFile) {

		return ResponseEntity.ok(s3StorageService.uploadOnS3(multipartFile, ServiceConst.S3_PROFILE_USER_DIR_PATH));
	}

	@PostMapping("/images/profile/pet")
	public ResponseEntity<?> uploadPetImage(@RequestParam("image") MultipartFile multipartFile) {

		return ResponseEntity.ok(s3StorageService.uploadOnS3(multipartFile, ServiceConst.S3_PROFILE_PET_DIR_PATH));
	}
}
