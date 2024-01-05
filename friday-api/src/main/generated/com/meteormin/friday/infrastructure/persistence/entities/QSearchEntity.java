package com.meteormin.friday.infrastructure.persistence.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSearchEntity is a Querydsl query type for SearchEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchEntity extends EntityPathBase<SearchEntity> {

    private static final long serialVersionUID = -671371272L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSearchEntity searchEntity = new QSearchEntity("searchEntity");

    public final com.meteormin.friday.infrastructure.persistence.QBaseEntity _super = new com.meteormin.friday.infrastructure.persistence.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final QHostEntity host;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath publish = createBoolean("publish");

    public final StringPath query = createString("query");

    public final StringPath queryKey = createString("queryKey");

    public final ListPath<SearchFileEntity, QSearchFileEntity> searchFiles = this.<SearchFileEntity, QSearchFileEntity>createList("searchFiles", SearchFileEntity.class, QSearchFileEntity.class, PathInits.DIRECT2);

    public final StringPath shortUrl = createString("shortUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QSearchEntity(String variable) {
        this(SearchEntity.class, forVariable(variable), INITS);
    }

    public QSearchEntity(Path<? extends SearchEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSearchEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSearchEntity(PathMetadata metadata, PathInits inits) {
        this(SearchEntity.class, metadata, inits);
    }

    public QSearchEntity(Class<? extends SearchEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.host = inits.isInitialized("host") ? new QHostEntity(forProperty("host"), inits.get("host")) : null;
    }

}

