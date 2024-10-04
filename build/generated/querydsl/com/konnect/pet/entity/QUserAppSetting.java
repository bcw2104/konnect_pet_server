package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAppSetting is a Querydsl query type for UserAppSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAppSetting extends EntityPathBase<UserAppSetting> {

    private static final long serialVersionUID = -949643283L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAppSetting userAppSetting = new QUserAppSetting("userAppSetting");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final BooleanPath communityYn = createBoolean("communityYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final BooleanPath friendYn = createBoolean("friendYn");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final BooleanPath messageYn = createBoolean("messageYn");

    public final BooleanPath serviceYn = createBoolean("serviceYn");

    public final QUser user;

    public final BooleanPath walkingYn = createBoolean("walkingYn");

    public QUserAppSetting(String variable) {
        this(UserAppSetting.class, forVariable(variable), INITS);
    }

    public QUserAppSetting(Path<? extends UserAppSetting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAppSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAppSetting(PathMetadata metadata, PathInits inits) {
        this(UserAppSetting.class, metadata, inits);
    }

    public QUserAppSetting(Class<? extends UserAppSetting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

