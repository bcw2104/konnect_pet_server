package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserTermsAgreement is a Querydsl query type for UserTermsAgreement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserTermsAgreement extends EntityPathBase<UserTermsAgreement> {

    private static final long serialVersionUID = -184146879L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserTermsAgreement userTermsAgreement = new QUserTermsAgreement("userTermsAgreement");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final BooleanPath agreedYn = createBoolean("agreedYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QTermsGroup termsGroup;

    public final QUser user;

    public QUserTermsAgreement(String variable) {
        this(UserTermsAgreement.class, forVariable(variable), INITS);
    }

    public QUserTermsAgreement(Path<? extends UserTermsAgreement> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserTermsAgreement(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserTermsAgreement(PathMetadata metadata, PathInits inits) {
        this(UserTermsAgreement.class, metadata, inits);
    }

    public QUserTermsAgreement(Class<? extends UserTermsAgreement> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.termsGroup = inits.isInitialized("termsGroup") ? new QTermsGroup(forProperty("termsGroup")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

