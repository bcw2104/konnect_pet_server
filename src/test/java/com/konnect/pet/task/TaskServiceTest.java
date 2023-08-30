package com.konnect.pet.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@Transactional
@RequiredArgsConstructor
public class TaskServiceTest {

	@Autowired
	private TaskService taskService;
	
	@Test
	public void cleanRemovedPostAndComment_test() {
		taskService.cleanRemovedPostAndComment();
	}
}
