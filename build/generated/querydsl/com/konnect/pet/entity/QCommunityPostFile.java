package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityPostFile is a Querydsl query type for CommunityPostFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityPostFile extends EntityPathBase<CommunityPostFile> {

    private static final long serialVersionUID = 198155154L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityPostFile communityPostFile = new QCommunityPostFile("communityPostFile");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QCommunityPost post;

    public final QUser user;

    public QCommunityPostFile(String variable) {
        this(CommunityPostFile.class, forVariable(variable), INITS);
    }

    public QCommunityPostFile(Path<? extends CommunityPostFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityPostFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityPostFile(PathMetadata metadata, PathInits inits) {
        this(CommunityPostFile.class, metadata, inits);
    }

    public QCommunityPostFile(Class<? extends CommunityPostFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QCommunityPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

