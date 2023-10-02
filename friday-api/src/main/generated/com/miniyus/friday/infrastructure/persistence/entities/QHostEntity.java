package com.miniyus.friday.infrastructure.persistence.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHostEntity is a Querydsl query type for HostEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHostEntity extends EntityPathBase<HostEntity> {

    private static final long serialVersionUID = -1796832142L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHostEntity hostEntity = new QHostEntity("hostEntity");

    public final com.miniyus.friday.infrastructure.persistence.QBaseEntity _super = new com.miniyus.friday.infrastructure.persistence.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final StringPath host = createString("host");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath path = createString("path");

    public final BooleanPath publish = createBoolean("publish");

    public final ListPath<SearchEntity, QSearchEntity> searches = this.<SearchEntity, QSearchEntity>createList("searches", SearchEntity.class, QSearchEntity.class, PathInits.DIRECT2);

    public final StringPath summary = createString("summary");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUserEntity user;

    public QHostEntity(String variable) {
        this(HostEntity.class, forVariable(variable), INITS);
    }

    public QHostEntity(Path<? extends HostEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHostEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHostEntity(PathMetadata metadata, PathInits inits) {
        this(HostEntity.class, metadata, inits);
    }

    public QHostEntity(Class<? extends HostEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUserEntity(forProperty("user")) : null;
    }

}

