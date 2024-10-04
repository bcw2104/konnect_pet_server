package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserWalkingFootprint is a Querydsl query type for UserWalkingFootprint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserWalkingFootprint extends EntityPathBase<UserWalkingFootprint> {

    private static final long serialVersionUID = 1895160324L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserWalkingFootprint userWalkingFootprint = new QUserWalkingFootprint("userWalkingFootprint");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public final QUser user;

    public final QUserWalkingHistory userWalkingHistory;

    public QUserWalkingFootprint(String variable) {
        this(UserWalkingFootprint.class, forVariable(variable), INITS);
    }

    public QUserWalkingFootprint(Path<? extends UserWalkingFootprint> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserWalkingFootprint(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserWalkingFootprint(PathMetadata metadata, PathInits inits) {
        this(UserWalkingFootprint.class, metadata, inits);
    }

    public QUserWalkingFootprint(Class<? extends UserWalkingFootprint> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.userWalkingHistory = inits.isInitialized("userWalkingHistory") ? new QUserWalkingHistory(forProperty("userWalkingHistory"), inits.get("userWalkingHistory")) : null;
    }

}

