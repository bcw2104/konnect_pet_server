package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEventRewardPolicy is a Querydsl query type for EventRewardPolicy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventRewardPolicy extends EntityPathBase<EventRewardPolicy> {

    private static final long serialVersionUID = 304447304L;

    public static final QEventRewardPolicy eventRewardPolicy = new QEventRewardPolicy("eventRewardPolicy");

    public final QBaseAutoSetAdminEntity _super = new QBaseAutoSetAdminEntity(this);

    public final BooleanPath activeYn = createBoolean("activeYn");

    public final NumberPath<Integer> balance = createNumber("balance", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final StringPath historyType = createString("historyType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath pointType = createString("pointType");

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public QEventRewardPolicy(String variable) {
        super(EventRewardPolicy.class, forVariable(variable));
    }

    public QEventRewardPolicy(Path<? extends EventRewardPolicy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEventRewardPolicy(PathMetadata metadata) {
        super(EventRewardPolicy.class, metadata);
    }

}

