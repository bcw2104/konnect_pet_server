package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserFriend is a Querydsl query type for UserFriend
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserFriend extends EntityPathBase<UserFriend> {

    private static final long serialVersionUID = -100590436L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserFriend userFriend = new QUserFriend("userFriend");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QUser fromUser;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath status = createString("status");

    public final QUser toUser;

    public QUserFriend(String variable) {
        this(UserFriend.class, forVariable(variable), INITS);
    }

    public QUserFriend(Path<? extends UserFriend> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserFriend(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserFriend(PathMetadata metadata, PathInits inits) {
        this(UserFriend.class, metadata, inits);
    }

    public QUserFriend(Class<? extends UserFriend> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromUser = inits.isInitialized("fromUser") ? new QUser(forProperty("fromUser")) : null;
        this.toUser = inits.isInitialized("toUser") ? new QUser(forProperty("toUser")) : null;
    }

}

