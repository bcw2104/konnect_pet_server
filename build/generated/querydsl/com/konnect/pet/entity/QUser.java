package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 967271518L;

    public static final QUser user = new QUser("user");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final StringPath aktId = createString("aktId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath deviceModel = createString("deviceModel");

    public final StringPath deviceOs = createString("deviceOs");

    public final StringPath deviceOsVersion = createString("deviceOsVersion");

    public final StringPath deviceToken = createString("deviceToken");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastLoginDate = createDateTime("lastLoginDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final BooleanPath marketingYn = createBoolean("marketingYn");

    public final StringPath nationCode = createString("nationCode");

    public final StringPath password = createString("password");

    public final EnumPath<com.konnect.pet.enums.PlatformType> platform = createEnum("platform", com.konnect.pet.enums.PlatformType.class);

    public final StringPath recommendCode = createString("recommendCode");

    public final StringPath residenceAddress = createString("residenceAddress");

    public final StringPath residenceCity = createString("residenceCity");

    public final StringPath residenceCoords = createString("residenceCoords");

    public final EnumPath<com.konnect.pet.enums.Roles> role = createEnum("role", com.konnect.pet.enums.Roles.class);

    public final StringPath status = createString("status");

    public final StringPath telEnc = createString("telEnc");

    public final StringPath telHash = createString("telHash");

    public final StringPath telMask = createString("telMask");

    public final ListPath<UserPet, QUserPet> userPets = this.<UserPet, QUserPet>createList("userPets", UserPet.class, QUserPet.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

