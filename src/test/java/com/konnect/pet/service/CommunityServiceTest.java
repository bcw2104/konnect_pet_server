package com.konnect.pet.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CommunityServiceTest {

	@Autowired
	private CommunityService communityService;

	@Test
	void dLock() throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(10);

		for (int i = 1; i <= 10; i++) {
			service.execute(() -> {
				communityService.changePostLikeT(3L);
			});
		}
		service.shutdown();
		service.awaitTermination(6, TimeUnit.MINUTES);
		
		log.info("finish");
	}
}
