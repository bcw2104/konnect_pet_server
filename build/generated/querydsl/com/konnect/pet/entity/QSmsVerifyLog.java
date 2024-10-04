package com.konnect.pet.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSmsVerifyLog is a Querydsl query type for SmsVerifyLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSmsVerifyLog extends EntityPathBase<SmsVerifyLog> {

    private static final long serialVersionUID = -1437847483L;

    public static final QSmsVerifyLog smsVerifyLog = new QSmsVerifyLog("smsVerifyLog");

    public final QBaseAutoSetEntity _super = new QBaseAutoSetEntity(this);

    public final BooleanPath consumedYn = createBoolean("consumedYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath locationCode = createString("locationCode");

    public final BooleanPath successYn = createBoolean("successYn");

    public final StringPath verifiyCode = createString("verifiyCode");

    public QSmsVerifyLog(String variable) {
        super(SmsVerifyLog.class, forVariable(variable));
    }

    public QSmsVerifyLog(Path<? extends SmsVerifyLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSmsVerifyLog(PathMetadata metadata) {
        super(SmsVerifyLog.class, metadata);
    }

}

