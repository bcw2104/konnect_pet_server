package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserDailyAccessLog is a Querydsl query type for UserDailyAccessLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserDailyAccessLog extends EntityPathBase<UserDailyAccessLog> {

    private static final long serialVersionUID = 1148718373L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserDailyAccessLog userDailyAccessLog = new QUserDailyAccessLog("userDailyAccessLog");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath deviceModel = createString("deviceModel");

    public final StringPath deviceOs = createString("deviceOs");

    public final StringPath deviceOsVersion = createString("deviceOsVersion");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QUser user;

    public QUserDailyAccessLog(String variable) {
        this(UserDailyAccessLog.class, forVariable(variable), INITS);
    }

    public QUserDailyAccessLog(Path<? extends UserDailyAccessLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserDailyAccessLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserDailyAccessLog(PathMetadata metadata, PathInits inits) {
        this(UserDailyAccessLog.class, metadata, inits);
    }

    public QUserDailyAccessLog(Class<? extends UserDailyAccessLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

