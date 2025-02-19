package com.konnect.pet.controller;

import java.util.List;

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
	public ResponseEntity<?> uploadUserImage(@RequestPart("image") MultipartFile multipartFile) {

		return ResponseEntity.ok(s3StorageService.uploadOnS3(multipartFile, ServiceConst.S3_PROFILE_USER_DIR_PATH));
	}

	@PostMapping("/images/profile/pet")
	public ResponseEntity<?> uploadPetImage(@RequestPart("image") MultipartFile multipartFile) {
		
		return ResponseEntity.ok(s3StorageService.uploadOnS3(multipartFile, ServiceConst.S3_PROFILE_PET_DIR_PATH));
	
	}
	@PostMapping("/images/community/post")
	public ResponseEntity<?> uploadPostImage(@RequestPart(value="images") List<MultipartFile> multipartFiles) {
		
		return ResponseEntity.ok(s3StorageService.uploadMultiOnS3(multipartFiles, ServiceConst.S3_COMMUNITY_POST_DIR_PATH));
	}
	
	@PostMapping("/images/community/comment")
	public ResponseEntity<?> uploadCommentImage(@RequestPart(value="image") MultipartFile multipartFile) {
		
		return ResponseEntity.ok(s3StorageService.uploadOnS3(multipartFile, ServiceConst.S3_COMMUNITY_COMMENT_DIR_PATH));
	}
}
