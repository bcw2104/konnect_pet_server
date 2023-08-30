package com.konnect.pet.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S", defaultLockAtLeastFor = "PT30S")
@Configuration
public class DatebaseConfig {
	@Autowired
	private DataSource dataSource;
	private static final String SHED_TABLE_NAME = "system_shedlock";

	@Bean
	public LockProvider lockProvider() {
		return new JdbcTemplateLockProvider(dataSource, SHED_TABLE_NAME);
	}
}
