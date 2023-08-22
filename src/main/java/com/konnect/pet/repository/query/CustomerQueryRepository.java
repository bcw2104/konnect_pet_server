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

	public List<QnaDto> findQnas(Long userId, String type, int limit, int offset) {
		return queryFactory
				.select(Projections
						.constructor(QnaDto.class, qna.id, qna.category, qna.title, qna.createdDate, qna.answeredDate))
				.from(qna)
				.where(qna.user.id.eq(userId),
						(type.equals("question") ? qna.answeredDate.isNull() : qna.answeredDate.isNotNull()))
				.orderBy(qna.id.desc()).limit(limit).offset(offset).fetch();
	}
}
