package com.konnect.pet.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseAutoSetEntity {

	@Column(updatable = false, nullable = false)
	private LocalDateTime createdDate;
	@Column(nullable = false)
	private LocalDateTime lastModifiedDate;

	@PrePersist
	public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        lastModifiedDate = now;
    }

	@PreUpdate
	public void preUpdate() {
		LocalDateTime now = LocalDateTime.now();
		lastModifiedDate = now;
	}
}
