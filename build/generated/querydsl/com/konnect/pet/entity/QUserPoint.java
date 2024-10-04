package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPoint is a Querydsl query type for UserPoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPoint extends EntityPathBase<UserPoint> {

    private static final long serialVersionUID = 5901266L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPoint userPoint = new QUserPoint("userPoint");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final NumberPath<Integer> balance = createNumber("balance", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath pointType = createString("pointType");

    public final QUser user;

    public QUserPoint(String variable) {
        this(UserPoint.class, forVariable(variable), INITS);
    }

    public QUserPoint(Path<? extends UserPoint> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPoint(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPoint(PathMetadata metadata, PathInits inits) {
        this(UserPoint.class, metadata, inits);
    }

    public QUserPoint(Class<? extends UserPoint> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

