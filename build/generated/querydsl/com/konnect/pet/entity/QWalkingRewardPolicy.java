package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWalkingRewardPolicy is a Querydsl query type for WalkingRewardPolicy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWalkingRewardPolicy extends EntityPathBase<WalkingRewardPolicy> {

    private static final long serialVersionUID = 1336679527L;

    public static final QWalkingRewardPolicy walkingRewardPolicy = new QWalkingRewardPolicy("walkingRewardPolicy");

    public final QBaseAutoSetAdminEntity _super = new QBaseAutoSetAdminEntity(this);

    public final BooleanPath activeYn = createBoolean("activeYn");

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> maxRewardAmountPerPeriod = createNumber("maxRewardAmountPerPeriod", Integer.class);

    public final NumberPath<Integer> maxRewardAmountPerWalking = createNumber("maxRewardAmountPerWalking", Integer.class);

    public final StringPath peroidType = createString("peroidType");

    public final NumberPath<Integer> pointPerUnit = createNumber("pointPerUnit", Integer.class);

    public final StringPath pointType = createString("pointType");

    public final StringPath policyName = createString("policyName");

    public final NumberPath<Integer> provideUnit = createNumber("provideUnit", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> rewardProvideDate = createDateTime("rewardProvideDate", java.time.LocalDateTime.class);

    public final StringPath rewardProvideType = createString("rewardProvideType");

    public final StringPath rewardType = createString("rewardType");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public QWalkingRewardPolicy(String variable) {
        super(WalkingRewardPolicy.class, forVariable(variable));
    }

    public QWalkingRewardPolicy(Path<? extends WalkingRewardPolicy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWalkingRewardPolicy(PathMetadata metadata) {
        super(WalkingRewardPolicy.class, metadata);
    }

}

