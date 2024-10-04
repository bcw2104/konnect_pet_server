package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityPostReportHistory is a Querydsl query type for CommunityPostReportHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityPostReportHistory extends EntityPathBase<CommunityPostReportHistory> {

    private static final long serialVersionUID = 926578538L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityPostReportHistory communityPostReportHistory = new QCommunityPostReportHistory("communityPostReportHistory");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QCommunityPost post;

    public final StringPath reportType = createString("reportType");

    public final QUser user;

    public QCommunityPostReportHistory(String variable) {
        this(CommunityPostReportHistory.class, forVariable(variable), INITS);
    }

    public QCommunityPostReportHistory(Path<? extends CommunityPostReportHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityPostReportHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityPostReportHistory(PathMetadata metadata, PathInits inits) {
        this(CommunityPostReportHistory.class, metadata, inits);
    }

    public QCommunityPostReportHistory(Class<? extends CommunityPostReportHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QCommunityPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

