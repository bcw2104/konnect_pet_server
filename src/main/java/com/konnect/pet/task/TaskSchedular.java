package com.konnect.pet.task;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "application.scheduler", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TaskSchedular {

	private final TaskService taskService;
	private final TransactionTemplate transactionTemplate;

	public TaskSchedular(PlatformTransactionManager manager, TaskService taskService) {
		this.taskService = taskService;
		this.transactionTemplate = new TransactionTemplate(manager);
	}

	@Scheduled(cron = "0/10 * * * * *")
	@SchedulerLock(name = "TASK_TEST", lockAtLeastFor = "PT5S", lockAtMostFor = "PT5S")
	public void taskTest() {
		TaskExecutor executor = new TaskExecutor("TASK_TEST");
		executor.run(() -> {
			log.info("Doing task");
		});
	}

	@Scheduled(cron = "0 0 2 * * *")
	@SchedulerLock(name = "TASK_REMOVED_POST_COMMENT_CLEANER", lockAtLeastFor = "PT11H", lockAtMostFor = "PT11H")
	public void taskRemovedPostCommentCleaner() {
		TaskExecutor executor = new TaskExecutor("TASK_REMOVED_POST_COMMENT_CLEANER", transactionTemplate);
		executor.run(() -> {
			log.info("Doing tx task");
		});
	}
}
