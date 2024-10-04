package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProperties is a Querydsl query type for Properties
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProperties extends EntityPathBase<Properties> {

    private static final long serialVersionUID = 2050493318L;

    public static final QProperties properties = new QProperties("properties");

    public final StringPath description = createString("description");

    public final StringPath pKey = createString("pKey");

    public final StringPath pKeyGroup = createString("pKeyGroup");

    public final StringPath pValue = createString("pValue");

    public QProperties(String variable) {
        super(Properties.class, forVariable(variable));
    }

    public QProperties(Path<? extends Properties> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProperties(PathMetadata metadata) {
        super(Properties.class, metadata);
    }

}

