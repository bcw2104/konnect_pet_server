package com.konnect.pet;

import static com.konnect.pet.entity.QTestEntity.testEntity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.konnect.pet.entity.TestEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		TestEntity result = queryFactory.selectFrom(testEntity).where(testEntity.name.eq("Test1")).fetchOne();

		assertThat(result).isEqualTo(t);
	}

}
