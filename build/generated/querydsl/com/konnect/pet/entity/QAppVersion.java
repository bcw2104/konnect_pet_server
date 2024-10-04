package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAppVersion is a Querydsl query type for AppVersion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppVersion extends EntityPathBase<AppVersion> {

    private static final long serialVersionUID = 165691850L;

    public static final QAppVersion appVersion = new QAppVersion("appVersion");

    public final QBaseAutoSetAdminEntity _super = new QBaseAutoSetAdminEntity(this);

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final BooleanPath forcedYn = createBoolean("forcedYn");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final DateTimePath<java.time.LocalDateTime> releasedDate = createDateTime("releasedDate", java.time.LocalDateTime.class);

    public final StringPath version = createString("version");

    public QAppVersion(String variable) {
        super(AppVersion.class, forVariable(variable));
    }

    public QAppVersion(Path<? extends AppVersion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAppVersion(PathMetadata metadata) {
        super(AppVersion.class, metadata);
    }

}

