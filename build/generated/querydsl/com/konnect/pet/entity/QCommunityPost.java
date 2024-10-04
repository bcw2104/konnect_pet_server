package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityPost is a Querydsl query type for CommunityPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityPost extends EntityPathBase<CommunityPost> {

    private static final long serialVersionUID = -128869130L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityPost communityPost = new QCommunityPost("communityPost");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final BooleanPath blockedYn = createBoolean("blockedYn");

    public final QCommunityCategory category;

    public final NumberPath<Integer> commentCount = createNumber("commentCount", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ListPath<CommunityPostFile, QCommunityPostFile> files = this.<CommunityPostFile, QCommunityPostFile>createList("files", CommunityPostFile.class, QCommunityPostFile.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> removedDate = createDateTime("removedDate", java.time.LocalDateTime.class);

    public final BooleanPath removedYn = createBoolean("removedYn");

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    public final QUser user;

    public QCommunityPost(String variable) {
        this(CommunityPost.class, forVariable(variable), INITS);
    }

    public QCommunityPost(Path<? extends CommunityPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityPost(PathMetadata metadata, PathInits inits) {
        this(CommunityPost.class, metadata, inits);
    }

    public QCommunityPost(Class<? extends CommunityPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCommunityCategory(forProperty("category")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

