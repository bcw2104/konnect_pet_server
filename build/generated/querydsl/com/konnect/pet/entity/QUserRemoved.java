package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserRemoved is a Querydsl query type for UserRemoved
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserRemoved extends EntityPathBase<UserRemoved> {

    private static final long serialVersionUID = -1426373086L;

    public static final QUserRemoved userRemoved = new QUserRemoved("userRemoved");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath deviceModel = createString("deviceModel");

    public final StringPath deviceOs = createString("deviceOs");

    public final StringPath deviceOsVersion = createString("deviceOsVersion");

    public final StringPath deviceToken = createString("deviceToken");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath nationCode = createString("nationCode");

    public final StringPath password = createString("password");

    public final EnumPath<com.konnect.pet.enums.PlatformType> platform = createEnum("platform", com.konnect.pet.enums.PlatformType.class);

    public final EnumPath<com.konnect.pet.enums.Roles> role = createEnum("role", com.konnect.pet.enums.Roles.class);

    public final StringPath telEnc = createString("telEnc");

    public final StringPath telHash = createString("telHash");

    public final StringPath telMask = createString("telMask");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserRemoved(String variable) {
        super(UserRemoved.class, forVariable(variable));
    }

    public QUserRemoved(Path<? extends UserRemoved> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRemoved(PathMetadata metadata) {
        super(UserRemoved.class, metadata);
    }

}

