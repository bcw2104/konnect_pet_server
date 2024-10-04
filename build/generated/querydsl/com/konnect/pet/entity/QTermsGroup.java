package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTermsGroup is a Querydsl query type for TermsGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTermsGroup extends EntityPathBase<TermsGroup> {

    private static final long serialVersionUID = 1918506891L;

    public static final QTermsGroup termsGroup = new QTermsGroup("termsGroup");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final StringPath content = createString("content");

    public final NumberPath<Long> createdBy = createNumber("createdBy", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> lastModifiedBy = createNumber("lastModifiedBy", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath locationCode = createString("locationCode");

    public final StringPath name = createString("name");

    public final BooleanPath requiredYn = createBoolean("requiredYn");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final ListPath<Terms, QTerms> terms = this.<Terms, QTerms>createList("terms", Terms.class, QTerms.class, PathInits.DIRECT2);

    public final BooleanPath visibleYn = createBoolean("visibleYn");

    public QTermsGroup(String variable) {
        super(TermsGroup.class, forVariable(variable));
    }

    public QTermsGroup(Path<? extends TermsGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTermsGroup(PathMetadata metadata) {
        super(TermsGroup.class, metadata);
    }

}

