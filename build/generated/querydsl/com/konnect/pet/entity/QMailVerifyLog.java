package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMailVerifyLog is a Querydsl query type for MailVerifyLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMailVerifyLog extends EntityPathBase<MailVerifyLog> {

    private static final long serialVersionUID = -1023297311L;

    public static final QMailVerifyLog mailVerifyLog = new QMailVerifyLog("mailVerifyLog");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final BooleanPath consumedYn = createBoolean("consumedYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath locationCode = createString("locationCode");

    public final StringPath verifiyCode = createString("verifiyCode");

    public QMailVerifyLog(String variable) {
        super(MailVerifyLog.class, forVariable(variable));
    }

    public QMailVerifyLog(Path<? extends MailVerifyLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMailVerifyLog(PathMetadata metadata) {
        super(MailVerifyLog.class, metadata);
    }

}

