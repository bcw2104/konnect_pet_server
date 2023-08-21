package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QQna.qna;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.QnaDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomerQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<QnaDto> findQnaByUserId(Long userId) {
		return queryFactory
				.select(Projections.constructor(QnaDto.class, qna.id,qna.category,qna.title,qna.answeredDate))
				.from(qna)
				.where(qna.user.id.eq(userId))
				.orderBy(qna.id.desc()).fetch();

	}
}
