package com.konnect.pet.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.Qna;

public interface QnaRepository extends JpaRepository<Qna, Long> {

	@Query("select q from Qna q where q.user.id = :userId")
	List<Qna> findByUserId(@Param("userId") Long userId);
	
	@Query("select count(q) from Qna q where q.user.id = :userId and q.createdDate >= :afterDate")
	int countByUserIdAndAfterDate(@Param("userId") Long userId, @Param("afterDate") LocalDateTime afterDate);
}
