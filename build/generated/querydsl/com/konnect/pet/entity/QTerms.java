package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTerms is a Querydsl query type for Terms
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTerms extends EntityPathBase<Terms> {

    private static final long serialVersionUID = -80682156L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTerms terms = new QTerms("terms");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final StringPath content = createString("content");

    public final NumberPath<Long> createdBy = createNumber("createdBy", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> lastModifiedBy = createNumber("lastModifiedBy", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final QTermsGroup termsGroup;

    public final BooleanPath visibleYn = createBoolean("visibleYn");

    public QTerms(String variable) {
        this(Terms.class, forVariable(variable), INITS);
    }

    public QTerms(Path<? extends Terms> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTerms(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTerms(PathMetadata metadata, PathInits inits) {
        this(Terms.class, metadata, inits);
    }

    public QTerms(Class<? extends Terms> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.termsGroup = inits.isInitialized("termsGroup") ? new QTermsGroup(forProperty("termsGroup")) : null;
    }

}

