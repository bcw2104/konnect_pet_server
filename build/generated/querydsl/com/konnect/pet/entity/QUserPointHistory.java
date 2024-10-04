package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPointHistory is a Querydsl query type for UserPointHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPointHistory extends EntityPathBase<UserPointHistory> {

    private static final long serialVersionUID = 1149427810L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPointHistory userPointHistory = new QUserPointHistory("userPointHistory");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final NumberPath<Integer> balance = createNumber("balance", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath historyType = createString("historyType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath pointType = createString("pointType");

    public final QUser user;

    public QUserPointHistory(String variable) {
        this(UserPointHistory.class, forVariable(variable), INITS);
    }

    public QUserPointHistory(Path<? extends UserPointHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPointHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPointHistory(PathMetadata metadata, PathInits inits) {
        this(UserPointHistory.class, metadata, inits);
    }

    public QUserPointHistory(Class<? extends UserPointHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

