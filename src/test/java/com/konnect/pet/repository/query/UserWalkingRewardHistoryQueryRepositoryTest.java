package com.konnect.pet.repository.query;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.querydsl.core.group.Group;

@SpringBootTest
public class UserWalkingRewardHistoryQueryRepositoryTest {

    @Autowired
    UserWalkingRewardHistoryQueryRepository historyQueryRepository;

    @Test
    public void testFindUserCurrentRewardTotalAmount() {
        Map<Long, Integer> findUserCurrentRewardTotalAmount = historyQueryRepository.findUserCurrentRewardTotalAmount(20L);

    }

}
