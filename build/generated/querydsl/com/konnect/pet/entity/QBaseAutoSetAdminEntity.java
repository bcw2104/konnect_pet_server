package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseAutoSetAdminEntity is a Querydsl query type for BaseAutoSetAdminEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseAutoSetAdminEntity extends EntityPathBase<BaseAutoSetAdminEntity> {

    private static final long serialVersionUID = 492620899L;

    public static final QBaseAutoSetAdminEntity baseAutoSetAdminEntity = new QBaseAutoSetAdminEntity("baseAutoSetAdminEntity");

    public final NumberPath<Long> createdBy = createNumber("createdBy", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> lastModifiedBy = createNumber("lastModifiedBy", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", java.time.LocalDateTime.class);

    public QBaseAutoSetAdminEntity(String variable) {
        super(BaseAutoSetAdminEntity.class, forVariable(variable));
    }

    public QBaseAutoSetAdminEntity(Path<? extends BaseAutoSetAdminEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseAutoSetAdminEntity(PathMetadata metadata) {
        super(BaseAutoSetAdminEntity.class, metadata);
    }

}

