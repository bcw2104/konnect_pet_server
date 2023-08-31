package com.konnect.pet.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityCommentDto {
	private Long commentId;

	private Long postId;

	private Long userId;

	private String nickname;

	private String profileImgPath;

	private String content;

	private int likeCount;

//	private boolean likeYn;

	private boolean removedYn;

	private LocalDateTime createdDate;

	private String imgPath;

	private Long parentCommentId;

	private List<CommunityCommentDto> childrens;

	public CommunityCommentDto(Long commentId, Long postId, Long userId, String nickname, String profileImgPath,
			String content, int likeCount, LocalDateTime createdDate, String imgPath, Long parentCommentId,
			boolean removedYn) {
		this.postId = postId;
		this.commentId = commentId;
		this.userId = userId;
		this.nickname = nickname;
		this.profileImgPath = profileImgPath;
		this.content = content;
		this.likeCount = likeCount;
		this.createdDate = createdDate;
		this.imgPath = imgPath;
		this.parentCommentId = parentCommentId;
		this.removedYn = removedYn;
	}

}
