package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityCommentLike is a Querydsl query type for CommunityCommentLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityCommentLike extends EntityPathBase<CommunityCommentLike> {

    private static final long serialVersionUID = 83450240L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityCommentLike communityCommentLike = new QCommunityCommentLike("communityCommentLike");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final QCommunityComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QUser user;

    public QCommunityCommentLike(String variable) {
        this(CommunityCommentLike.class, forVariable(variable), INITS);
    }

    public QCommunityCommentLike(Path<? extends CommunityCommentLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityCommentLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityCommentLike(PathMetadata metadata, PathInits inits) {
        this(CommunityCommentLike.class, metadata, inits);
    }

    public QCommunityCommentLike(Class<? extends CommunityCommentLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new QCommunityComment(forProperty("comment"), inits.get("comment")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

