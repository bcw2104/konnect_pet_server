package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserNotificationLog is a Querydsl query type for UserNotificationLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserNotificationLog extends EntityPathBase<UserNotificationLog> {

    private static final long serialVersionUID = -1496661477L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserNotificationLog userNotificationLog = new QUserNotificationLog("userNotificationLog");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QUser user;

    public final QUserNotification userNotification;

    public final BooleanPath visitedYn = createBoolean("visitedYn");

    public QUserNotificationLog(String variable) {
        this(UserNotificationLog.class, forVariable(variable), INITS);
    }

    public QUserNotificationLog(Path<? extends UserNotificationLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserNotificationLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserNotificationLog(PathMetadata metadata, PathInits inits) {
        this(UserNotificationLog.class, metadata, inits);
    }

    public QUserNotificationLog(Class<? extends UserNotificationLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
        this.userNotification = inits.isInitialized("userNotification") ? new QUserNotification(forProperty("userNotification")) : null;
    }

}

