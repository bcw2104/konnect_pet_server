package com.konnect.pet.repository.query;

import static com.konnect.pet.entity.QTerms.terms;
import static com.konnect.pet.entity.QTermsGroup.termsGroup;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.konnect.pet.dto.TermsDto;
import com.konnect.pet.dto.TermsGroupDto;
import com.konnect.pet.enums.code.LocationCode;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TermsQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<TermsGroupDto> findTermsGroupByLocationCodeAndVisibleYn(LocationCode locationCode, boolean visibleYn) {
		return queryFactory
				.select(Projections.constructor(TermsGroupDto.class, termsGroup.id, termsGroup.termsGroupName,
						termsGroup.termsGroupContent, termsGroup.requiredYn))
				.from(termsGroup)
				.where(termsGroup.visibleYn.eq(visibleYn), termsGroup.locationCode.eq(locationCode.getCode()))
				.orderBy(termsGroup.sortOrder.asc()).fetch();

	}

	public TermsDto findLastestTermsByTermsGroupsId(Long termsGroupId) {
		return queryFactory
				.select(Projections.constructor(TermsDto.class, terms.id, terms.termsGroup.id,
						terms.termsGroup.termsGroupName, terms.termsName, terms.termsContent,
						terms.termsGroup.requiredYn, terms.createdDate))
				.from(terms).innerJoin(terms.termsGroup, termsGroup).where(terms.termsGroup.id.eq(termsGroupId))
				.orderBy(terms.createdDate.desc()).limit(1).fetchOne();

	}
}
