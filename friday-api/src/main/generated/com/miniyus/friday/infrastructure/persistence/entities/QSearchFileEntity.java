package com.miniyus.friday.infrastructure.persistence.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSearchFileEntity is a Querydsl query type for SearchFileEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchFileEntity extends EntityPathBase<SearchFileEntity> {

    private static final long serialVersionUID = 617460686L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSearchFileEntity searchFileEntity = new QSearchFileEntity("searchFileEntity");

    public final com.miniyus.friday.infrastructure.persistence.QBaseEntity _super = new com.miniyus.friday.infrastructure.persistence.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QFileEntity file;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSearchEntity search;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSearchFileEntity(String variable) {
        this(SearchFileEntity.class, forVariable(variable), INITS);
    }

    public QSearchFileEntity(Path<? extends SearchFileEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSearchFileEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSearchFileEntity(PathMetadata metadata, PathInits inits) {
        this(SearchFileEntity.class, metadata, inits);
    }

    public QSearchFileEntity(Class<? extends SearchFileEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.file = inits.isInitialized("file") ? new QFileEntity(forProperty("file"), inits.get("file")) : null;
        this.search = inits.isInitialized("search") ? new QSearchEntity(forProperty("search"), inits.get("search")) : null;
    }

}

