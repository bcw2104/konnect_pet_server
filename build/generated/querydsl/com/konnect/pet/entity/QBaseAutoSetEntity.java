package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseAutoSetEntity is a Querydsl query type for BaseAutoSetEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseAutoSetEntity extends EntityPathBase<BaseAutoSetEntity> {

    private static final long serialVersionUID = 303383794L;

    public static final QBaseAutoSetEntity baseAutoSetEntity = new QBaseAutoSetEntity("baseAutoSetEntity");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", java.time.LocalDateTime.class);

    public QBaseAutoSetEntity(String variable) {
        super(BaseAutoSetEntity.class, forVariable(variable));
    }

    public QBaseAutoSetEntity(Path<? extends BaseAutoSetEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseAutoSetEntity(PathMetadata metadata) {
        super(BaseAutoSetEntity.class, metadata);
    }

}

