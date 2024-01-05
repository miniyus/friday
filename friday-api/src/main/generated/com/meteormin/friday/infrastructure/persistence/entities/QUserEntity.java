package com.meteormin.friday.infrastructure.persistence.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = 1851359003L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final com.meteormin.friday.infrastructure.persistence.QBaseEntity _super = new com.meteormin.friday.infrastructure.persistence.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final ListPath<FileEntity, QFileEntity> files = this.<FileEntity, QFileEntity>createList("files", FileEntity.class, QFileEntity.class, PathInits.DIRECT2);

    public final ListPath<HostEntity, QHostEntity> hosts = this.<HostEntity, QHostEntity>createList("hosts", HostEntity.class, QHostEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<LoginHistoryEntity, QLoginHistoryEntity> loginHistories = this.<LoginHistoryEntity, QLoginHistoryEntity>createList("loginHistories", LoginHistoryEntity.class, QLoginHistoryEntity.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final EnumPath<com.meteormin.friday.infrastructure.security.social.SocialProvider> provider = createEnum("provider", com.meteormin.friday.infrastructure.security.social.SocialProvider.class);

    public final EnumPath<com.meteormin.friday.users.domain.UserRole> role = createEnum("role", com.meteormin.friday.users.domain.UserRole.class);

    public final StringPath snsId = createString("snsId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

