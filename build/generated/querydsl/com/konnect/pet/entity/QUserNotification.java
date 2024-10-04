package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserNotification is a Querydsl query type for UserNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserNotification extends EntityPathBase<UserNotification> {

    private static final long serialVersionUID = -418143127L;

    public static final QUserNotification userNotification = new QUserNotification("userNotification");

    public final QBaseAutoSetAdminEntity _super = new QBaseAutoSetAdminEntity(this);

    public final BooleanPath activeYn = createBoolean("activeYn");

    public final StringPath category = createString("category");

    public final StringPath content = createString("content");

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath landingType = createString("landingType");

    public final StringPath landingUrl = createString("landingUrl");

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath notiType = createString("notiType");

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    public QUserNotification(String variable) {
        super(UserNotification.class, forVariable(variable));
    }

    public QUserNotification(Path<? extends UserNotification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserNotification(PathMetadata metadata) {
        super(UserNotification.class, metadata);
    }

}

