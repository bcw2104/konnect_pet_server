package com.konnect.pet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Configuration
public class QueryDslConfig {
	@Autowired
	private EntityManager em;

	@Bean
	JPAQueryFactory queryFactory() {
		 return new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
	}
}
