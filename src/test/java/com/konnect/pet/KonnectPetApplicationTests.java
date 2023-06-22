package com.konnect.pet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.entity.TestEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Transactional
@SpringBootTest
class KonnectPetApplicationTests {

	@Autowired
	EntityManager em;
	@Autowired
	JPAQueryFactory queryFactory;

	@Test
	void contextLoads() {
		TestEntity t = new TestEntity("Test1");
		em.persist(t);


	}

}
