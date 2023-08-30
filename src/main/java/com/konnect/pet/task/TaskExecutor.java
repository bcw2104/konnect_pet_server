package com.konnect.pet.task;

import java.time.LocalDateTime;

import org.springframework.transaction.support.TransactionTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskExecutor {
	private String taskName;
	private TransactionTemplate template;

	public TaskExecutor(String taskName) {
		this.taskName = taskName;
		this.template = null;
	}

	public TaskExecutor(String taskName, TransactionTemplate template) {
		this.taskName = taskName;
		this.template = template;
	}

	void run(Task task) {
		long start = System.currentTimeMillis();
		log.info("{} is started at {}", taskName, LocalDateTime.now());

		if (template != null) {
			template.executeWithoutResult(t -> {
				log.info("{}", t.isNewTransaction());
				task.logic();
			});
		} else {
			task.logic();
		}

		long end = System.currentTimeMillis();
		log.info("{} is finished at {} - time: {}ms", taskName, LocalDateTime.now(), end - start);
	}
}
