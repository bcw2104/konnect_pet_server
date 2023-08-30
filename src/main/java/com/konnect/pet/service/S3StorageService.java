package com.konnect.pet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.ListUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
	@Value("${cloud.aws.s3.public-path}")
	private String publicPath;

	private final AmazonS3Client amazonS3Client;

	public void removeOnS3(String path) {
		if (path != null && path.startsWith(publicPath) && path.contains(".")) {
			try {
				amazonS3Client.deleteObject(bucket, path);
			} catch (Exception e) {
				log.error("S3 Object remove Error");
			}
		}
	}

	public void removeMultiOnS3(List<String> paths) {
		List<KeyVersion> keys = new ArrayList<KeyVersion>();
		for (String path : paths) {
			if (path != null && path.startsWith(publicPath) && path.contains(".")) {
				keys.add(new KeyVersion(path));
			}
		}

		try {
			List<List<KeyVersion>> partitions = ListUtils.partition(keys, 500);

			for (List<KeyVersion> partition : partitions) {
				DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket).withKeys(keys)
						.withQuiet(false);
				amazonS3Client.deleteObjects(multiObjectDeleteRequest);
			}
		} catch (Exception e) {
			log.error("S3 Object remove Error");
		}
	}

	public ResponseDto uploadOnS3(MultipartFile uploadFile, String dirPath) {
		String origName = uploadFile.getOriginalFilename();
		String path = null;
		try {
			final String ext = origName.substring(origName.lastIndexOf('.'));
			final String saveFileName = getUuid() + ext;

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(uploadFile.getContentType());
			metadata.setContentLength(uploadFile.getSize());

			path = publicPath + "/" + dirPath + "/" + saveFileName;
			amazonS3Client.putObject(bucket, path, uploadFile.getInputStream(), metadata);
		} catch (Exception e) {
			log.error("S3 Object upload Error");
			throw new CustomResponseException(ResponseType.FAIL, e.getMessage());
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("imagePath", path);

		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	public ResponseDto uploadMultiOnS3(List<MultipartFile> multipartFiles, String dirPath) {
		List<String> paths = new ArrayList<String>();
		for (MultipartFile uploadFile : multipartFiles) {
			String origName = uploadFile.getOriginalFilename();
			String path = null;
			try {
				final String ext = origName.substring(origName.lastIndexOf('.'));
				final String saveFileName = getUuid() + ext;

				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(uploadFile.getContentType());
				metadata.setContentLength(uploadFile.getSize());

				path = publicPath + "/" + dirPath + "/" + saveFileName;
				amazonS3Client.putObject(bucket, path, uploadFile.getInputStream(), metadata);
				paths.add(path);
			} catch (Exception e) {
				log.error("S3 Object upload Error");
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("imagePaths", paths);
		return new ResponseDto(ResponseType.SUCCESS, resultMap);
	}

	private String getUuid() {
		String date = LocalDateTime.now().toString("yyyyMMdd");
		return UUID.randomUUID().toString().replaceAll("-", "") + "_" + date;
	}
}
