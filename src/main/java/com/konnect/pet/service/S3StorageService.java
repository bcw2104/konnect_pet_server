package com.konnect.pet.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.response.ResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageService {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	@Value("${cloud.aws.s3.url}")
	private String bucketUrl;

	private final AmazonS3Client amazonS3Client;

	public ResponseDto uploadOnS3(MultipartFile uploadFile, String dirPath) {
		String origName = uploadFile.getOriginalFilename();
		String url;
		try {
			final String ext = origName.substring(origName.lastIndexOf('.'));
			final String saveFileName = getUuid() + ext;

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(uploadFile.getContentType());
			metadata.setContentLength(uploadFile.getSize());

			amazonS3Client.putObject(bucket, saveFileName, uploadFile.getInputStream(), metadata);
			url = bucketUrl + "/" + saveFileName;
		} catch (Exception e) {
			throw new CustomResponseException(ResponseType.FAIL, e.getMessage());
		}

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("imageUrl", url);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	private String getUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
