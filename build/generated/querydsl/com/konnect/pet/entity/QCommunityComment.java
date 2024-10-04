package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityComment is a Querydsl query type for CommunityComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityComment extends EntityPathBase<CommunityComment> {

    private static final long serialVersionUID = 1902215753L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityComment communityComment = new QCommunityComment("communityComment");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final BooleanPath blockedYn = createBoolean("blockedYn");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgPath = createString("imgPath");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final QCommunityPost post;

    public final DateTimePath<java.time.LocalDateTime> removedDate = createDateTime("removedDate", java.time.LocalDateTime.class);

    public final BooleanPath removedYn = createBoolean("removedYn");

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    public final QUser user;

    public QCommunityComment(String variable) {
        this(CommunityComment.class, forVariable(variable), INITS);
    }

    public QCommunityComment(Path<? extends CommunityComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityComment(PathMetadata metadata, PathInits inits) {
        this(CommunityComment.class, metadata, inits);
    }

    public QCommunityComment(Class<? extends CommunityComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QCommunityPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

