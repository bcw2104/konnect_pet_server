package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserFriend;

import jakarta.persistence.LockModeType;

public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

	@Query("select u from UserFriend u where u.id = :id")
	Optional<UserFriend> findById(@Param("id") Long id);

	@Modifying
	@Query("delete from UserFriend u where u.fromUser.id = :userId or u.toUser.id = :userId")
	int deleteAllByUserId(@Param("userId") Long userId);

	@Query("select count(u) from UserFriend u where u.fromUser.id = :userId and u.status = :status")
	int countByFromUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

	@Query("select u from UserFriend u where u.fromUser.id = :fromUserId and u.toUser.id = :toUserId")
	Optional<UserFriend> findByFromUserIdAndToUserId(@Param("fromUserId") Long fromUserId,
			@Param("toUserId") Long toUserId);

	@Query("select u from UserFriend u join fetch u.toUser where u.fromUser.id = :userId and u.status = :status")
	List<UserFriend> findByFromUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

	@Query("select u from UserFriend u join fetch u.fromUser where u.toUser.id = :userId and u.status = :status")
	List<UserFriend> findByToUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}
