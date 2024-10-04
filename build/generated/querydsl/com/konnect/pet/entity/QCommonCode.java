package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommonCode is a Querydsl query type for CommonCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommonCode extends EntityPathBase<CommonCode> {

    private static final long serialVersionUID = -134423349L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommonCode commonCode = new QCommonCode("commonCode");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final StringPath codeDescription = createString("codeDescription");

    public final StringPath codeName = createString("codeName");

    public final com.konnect.pet.entity.embedded.QCommonCodePair codePair;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public QCommonCode(String variable) {
        this(CommonCode.class, forVariable(variable), INITS);
    }

    public QCommonCode(Path<? extends CommonCode> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommonCode(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommonCode(PathMetadata metadata, PathInits inits) {
        this(CommonCode.class, metadata, inits);
    }

    public QCommonCode(Class<? extends CommonCode> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.codePair = inits.isInitialized("codePair") ? new com.konnect.pet.entity.embedded.QCommonCodePair(forProperty("codePair")) : null;
    }

}

