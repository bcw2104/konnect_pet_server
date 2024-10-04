package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPet is a Querydsl query type for UserPet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPet extends EntityPathBase<UserPet> {

    private static final long serialVersionUID = 1050284001L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPet userPet = new QUserPet("userPet");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final StringPath birthDate = createString("birthDate");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgPath = createString("imgPath");

    public final BooleanPath inoculatedYn = createBoolean("inoculatedYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final BooleanPath neuteredYn = createBoolean("neuteredYn");

    public final StringPath species = createString("species");

    public final StringPath type = createString("type");

    public final QUser user;

    public final NumberPath<java.math.BigDecimal> weight = createNumber("weight", java.math.BigDecimal.class);

    public QUserPet(String variable) {
        this(UserPet.class, forVariable(variable), INITS);
    }

    public QUserPet(Path<? extends UserPet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPet(PathMetadata metadata, PathInits inits) {
        this(UserPet.class, metadata, inits);
    }

    public QUserPet(Class<? extends UserPet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

