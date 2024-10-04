package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserWalkingRewardHistory is a Querydsl query type for UserWalkingRewardHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserWalkingRewardHistory extends EntityPathBase<UserWalkingRewardHistory> {

    private static final long serialVersionUID = -171827478L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserWalkingRewardHistory userWalkingRewardHistory = new QUserWalkingRewardHistory("userWalkingRewardHistory");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final BooleanPath paymentYn = createBoolean("paymentYn");

    public final StringPath pointType = createString("pointType");

    public final StringPath rewardProvideType = createString("rewardProvideType");

    public final StringPath rewardType = createString("rewardType");

    public final QUser user;

    public final QUserWalkingHistory userWalkingHistory;

    public final QWalkingRewardPolicy walkingRewardPolicy;

    public QUserWalkingRewardHistory(String variable) {
        this(UserWalkingRewardHistory.class, forVariable(variable), INITS);
    }

    public QUserWalkingRewardHistory(Path<? extends UserWalkingRewardHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserWalkingRewardHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserWalkingRewardHistory(PathMetadata metadata, PathInits inits) {
        this(UserWalkingRewardHistory.class, metadata, inits);
    }

    public QUserWalkingRewardHistory(Class<? extends UserWalkingRewardHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.userWalkingHistory = inits.isInitialized("userWalkingHistory") ? new QUserWalkingHistory(forProperty("userWalkingHistory"), inits.get("userWalkingHistory")) : null;
        this.walkingRewardPolicy = inits.isInitialized("walkingRewardPolicy") ? new QWalkingRewardPolicy(forProperty("walkingRewardPolicy")) : null;
    }

}

