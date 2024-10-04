package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserWalkingFootprintCatchHistory is a Querydsl query type for UserWalkingFootprintCatchHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserWalkingFootprintCatchHistory extends EntityPathBase<UserWalkingFootprintCatchHistory> {

    private static final long serialVersionUID = 987880829L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserWalkingFootprintCatchHistory userWalkingFootprintCatchHistory = new QUserWalkingFootprintCatchHistory("userWalkingFootprintCatchHistory");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QUser user;

    public final QUserWalkingFootprint userWalkingFootprint;

    public final QUserWalkingHistory userWalkingHistory;

    public QUserWalkingFootprintCatchHistory(String variable) {
        this(UserWalkingFootprintCatchHistory.class, forVariable(variable), INITS);
    }

    public QUserWalkingFootprintCatchHistory(Path<? extends UserWalkingFootprintCatchHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserWalkingFootprintCatchHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserWalkingFootprintCatchHistory(PathMetadata metadata, PathInits inits) {
        this(UserWalkingFootprintCatchHistory.class, metadata, inits);
    }

    public QUserWalkingFootprintCatchHistory(Class<? extends UserWalkingFootprintCatchHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.userWalkingFootprint = inits.isInitialized("userWalkingFootprint") ? new QUserWalkingFootprint(forProperty("userWalkingFootprint"), inits.get("userWalkingFootprint")) : null;
        this.userWalkingHistory = inits.isInitialized("userWalkingHistory") ? new QUserWalkingHistory(forProperty("userWalkingHistory"), inits.get("userWalkingHistory")) : null;
    }

}

