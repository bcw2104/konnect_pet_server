package com.konnect.pet.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.konnect.pet.entity.CommunityPost;

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

	private String content;

	private int likeCount;

	private int commentCount;

	private boolean likeYn;

	private boolean removedYn;

	private boolean blockedYn;

	private LocalDateTime createdDate;

	private List<String> filePaths;

	public CommunityPostDto(Long postId, Long categoryId, String category, Long userId, String nickname,
			String profileImgPath, String content, int likeCount, int commentCount, LocalDateTime createdDate,
			boolean removedYn, boolean blockedYn) {

		this.postId = postId;
		this.categoryId = categoryId;
		this.category = category;
		this.userId = userId;
		this.nickname = nickname;
		this.profileImgPath = profileImgPath;
		this.content = removedYn ? "This post has been deleted."
				: blockedYn ? "This post has been deleted by administrator." : content;
		this.likeCount = likeCount;
		this.commentCount = commentCount;
		this.createdDate = createdDate;
		this.removedYn = removedYn;
		this.blockedYn = blockedYn;
	}
}
