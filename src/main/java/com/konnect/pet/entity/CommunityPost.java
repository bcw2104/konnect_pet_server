package com.konnect.pet.entity;

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
public class CommunityPost extends BaseAutoSetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_COMMUNITY_POST-CATEGORY_ID"), nullable = false)
	private CommunityCategory category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_COMMUNITY_POST-USER_ID"), nullable = false)
	private User user;

	@Column(length = 800, nullable = false)
	private String content;

	@ColumnDefault("0")
	private int likeCount;
	
	@ColumnDefault("0")
	private int commentCount;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<CommunityPostFile> files;
}
