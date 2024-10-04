package com.konnect.pet.entity.embedded;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommonCodePair is a Querydsl query type for CommonCodePair
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCommonCodePair extends BeanPath<CommonCodePair> {

    private static final long serialVersionUID = -1320606173L;

    public static final QCommonCodePair commonCodePair = new QCommonCodePair("commonCodePair");

    public final StringPath code = createString("code");

    public final StringPath codeGroup = createString("codeGroup");

    public QCommonCodePair(String variable) {
        super(CommonCodePair.class, forVariable(variable));
    }

    public QCommonCodePair(Path<? extends CommonCodePair> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommonCodePair(PathMetadata metadata) {
        super(CommonCodePair.class, metadata);
    }

}

