package com.konnect.pet.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.CommunityPostFile;

public interface CommunityPostFileRepository extends JpaRepository<CommunityPostFile, Long> {

	@Query("select pf.filePath from CommunityPostFile pf join pf.post p where p.removedYn = true and p.removedDate < :beforeDate")
	List<String> findRemovedFilePathByBeforeDate(@Param("beforeDate") LocalDateTime beforeDate);
	
	@Modifying
	@Query(value = "update community_post_file pf join community_post p on pf.post_id = p.post_id "
			+ "set pf.file_path = '*' where p.removed_yn = true and p.removed_date < :beforeDate",nativeQuery = true)
	int maskingRemovedByBeforeDate(@Param("beforeDate") LocalDateTime beforeDate);
}
