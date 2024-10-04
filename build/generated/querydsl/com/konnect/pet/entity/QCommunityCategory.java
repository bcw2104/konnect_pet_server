package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityCategory is a Querydsl query type for CommunityCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityCategory extends EntityPathBase<CommunityCategory> {

    private static final long serialVersionUID = -507926956L;

    public static final QCommunityCategory communityCategory = new QCommunityCategory("communityCategory");

    public final QBaseAutoSetAdminEntity _super = new QBaseAutoSetAdminEntity(this);

    public final BooleanPath activeYn = createBoolean("activeYn");

    public final StringPath category = createString("category");

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public QCommunityCategory(String variable) {
        super(CommunityCategory.class, forVariable(variable));
    }

    public QCommunityCategory(Path<? extends CommunityCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityCategory(PathMetadata metadata) {
        super(CommunityCategory.class, metadata);
    }

}

