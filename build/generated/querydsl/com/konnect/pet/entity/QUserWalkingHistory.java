package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserWalkingHistory is a Querydsl query type for UserWalkingHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserWalkingHistory extends EntityPathBase<UserWalkingHistory> {

    private static final long serialVersionUID = 1738506457L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserWalkingHistory userWalkingHistory = new QUserWalkingHistory("userWalkingHistory");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final ListPath<UserWalkingFootprintCatchHistory, QUserWalkingFootprintCatchHistory> footprintCatchHistories = this.<UserWalkingFootprintCatchHistory, QUserWalkingFootprintCatchHistory>createList("footprintCatchHistories", UserWalkingFootprintCatchHistory.class, QUserWalkingFootprintCatchHistory.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> meters = createNumber("meters", Integer.class);

    public final ListPath<UserWalkingRewardHistory, QUserWalkingRewardHistory> rewardHistories = this.<UserWalkingRewardHistory, QUserWalkingRewardHistory>createList("rewardHistories", UserWalkingRewardHistory.class, QUserWalkingRewardHistory.class, PathInits.DIRECT2);

    public final StringPath routes = createString("routes");

    public final NumberPath<Integer> seconds = createNumber("seconds", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final QUser user;

    public QUserWalkingHistory(String variable) {
        this(UserWalkingHistory.class, forVariable(variable), INITS);
    }

    public QUserWalkingHistory(Path<? extends UserWalkingHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserWalkingHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserWalkingHistory(PathMetadata metadata, PathInits inits) {
        this(UserWalkingHistory.class, metadata, inits);
    }

    public QUserWalkingHistory(Class<? extends UserWalkingHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

