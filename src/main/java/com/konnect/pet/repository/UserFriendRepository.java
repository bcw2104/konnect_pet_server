package com.konnect.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.konnect.pet.entity.UserFriend;

public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

	@Query("select u from UserFriend u where u.id = :id")
	Optional<UserFriend> findById(@Param("id") Long id);

	@Query("select count(u) from UserFriend u where u.fromUser.id = :userId")
	int countByUserId(@Param("userId") Long userId);

	@Query("select u from UserFriend u where u.fromUser.id = :fromUserId and u.toUser.id = :toUserId")
	Optional<UserFriend> findByFromUserIdAndToUserId(@Param("fromUserId") Long fromUserId,
			@Param("toUserId") Long toUserId);

	@Query("select u from UserFriend u join fetch u.toUser where u.fromUser.id = :userId")
	List<UserFriend> findByFromUserId(@Param("userId") Long userId);

	@Query("select u from UserFriend u join fetch u.toUser where u.toUser.id = :userId and u.status = :status")
	List<UserFriend> findByToUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}
