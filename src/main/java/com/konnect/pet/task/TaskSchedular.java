package com.konnect.pet.task;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.konnect.pet.annotation.ExecutionTimer;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "spring.task.scheduling", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TaskSchedular {

	private final TaskService taskService;
	private final TransactionTemplate transactionTemplate;

	public TaskSchedular(PlatformTransactionManager manager, TaskService taskService) {
		this.taskService = taskService;
		this.transactionTemplate = new TransactionTemplate(manager);
	}

	@ExecutionTimer(task = "TASK_TEST")
	@Scheduled(cron = "0 0/5 * * * *")
	@SchedulerLock(name = "TASK_TEST", lockAtLeastFor = "PT4M", lockAtMostFor = "PT4M")
	public void taskTest() {
		log.info("Doing task");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@ExecutionTimer(task = "TASK_REMOVED_POST_COMMENT_CLEANER")
	@Scheduled(cron = "0 0 2 * * *")
	@SchedulerLock(name = "TASK_REMOVED_POST_COMMENT_CLEANER", lockAtLeastFor = "PT11H", lockAtMostFor = "PT11H")
	public void taskRemovedPostCommentCleaner() {
		taskService.cleanRemovedPostAndComment();
	}
}
