package com.konnect.pet.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.konnect.pet.entity.embedded.CommonCodePair;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class CommunityComment extends BaseAutoSetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(name = "parent_comment_id")
	private Long parentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "FK_COMMUNITY_COMMENT-POST_ID"), nullable = false)
	private CommunityPost post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_COMMUNITY_COMMENT-USER_ID"), nullable = false)
	private User user;

	@Column(length = 800, nullable = false)
	private String content;
	
	@ColumnDefault("0")
	private int likeCount;
	
	private boolean removedYn;

	private LocalDateTime removedDate;

	@Column(length = 255)
	private String imgPath;
}
