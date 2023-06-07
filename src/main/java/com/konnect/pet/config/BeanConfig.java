package com.konnect.pet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Component
public class BeanConfig {
	@Autowired
	private EntityManager em;

	@Bean
	JPAQueryFactory queryFactory() {
		return new JPAQueryFactory(em);
	}
}
