package com.konnect.pet.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityPostDto {
	private Long postId;

	private Long categoryId;
	private String category;

	private Long userId;

	private String nickname;

	private String profileImgPath;

	private String residenceCity;

	private String content;

	private int likeCount;

	private int commentCount;

	private boolean likeYn;
	
	private LocalDateTime createdDate;

	private List<String> filePaths;

	public CommunityPostDto(Long postId, Long categoryId, String category, Long userId, String nickname, String profileImgPath,
			String residenceCity, String content, int likeCount, int commentCount,LocalDateTime createdDate) {
		this.postId = postId;
		this.categoryId = categoryId;
		this.category = category;
		this.userId = userId;
		this.nickname = nickname;
		this.profileImgPath = profileImgPath;
		this.residenceCity = residenceCity;
		this.content = content;
		this.likeCount = likeCount;
		this.commentCount = commentCount;
		this.createdDate = createdDate;
	}

}
