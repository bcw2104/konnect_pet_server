package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityCommentReportHistory is a Querydsl query type for CommunityCommentReportHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityCommentReportHistory extends EntityPathBase<CommunityCommentReportHistory> {

    private static final long serialVersionUID = -825290505L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityCommentReportHistory communityCommentReportHistory = new QCommunityCommentReportHistory("communityCommentReportHistory");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final QCommunityComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath reportType = createString("reportType");

    public final QUser user;

    public QCommunityCommentReportHistory(String variable) {
        this(CommunityCommentReportHistory.class, forVariable(variable), INITS);
    }

    public QCommunityCommentReportHistory(Path<? extends CommunityCommentReportHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityCommentReportHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityCommentReportHistory(PathMetadata metadata, PathInits inits) {
        this(CommunityCommentReportHistory.class, metadata, inits);
    }

    public QCommunityCommentReportHistory(Class<? extends CommunityCommentReportHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new QCommunityComment(forProperty("comment"), inits.get("comment")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

