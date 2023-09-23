package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.miniyus.friday.infrastructure.persistence.entities.QHostEntity.hostEntity;
import static com.miniyus.friday.infrastructure.persistence.entities.QUserEntity.userEntity;

import java.util.Objects;

@AllArgsConstructor
@Repository
public class HostRepositoryImpl implements QHostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<HostEntity> findAll(Host.HostFilter hostFilter, Pageable pageable) {
        var query = jpaQueryFactory.selectFrom(hostEntity);
        var hosts = whereHostFilter(query, hostFilter)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        var count = whereHostFilter(
            jpaQueryFactory.select(hostEntity.count()),
            hostFilter).fetchOne();

        return new PageImpl<>(hosts, pageable, Objects.requireNonNullElse(count, 0L));
    }

    private <T> JPAQuery<T> whereHostFilter(JPAQuery<T> query, Host.HostFilter filter) {
        if (filter.userId() != null && filter.userId().equals(0L)) {
            query.join(hostEntity.user, userEntity)
                .where(userEntity.id.eq(filter.userId()));
        }
        if (filter.summary() != null && !filter.summary().isEmpty()) {
            query.where(hostEntity.summary.contains(filter.summary()));
        }
        if (filter.description() != null && !filter.description().isEmpty()) {
            query.where(hostEntity.description.contains(filter.description()));
        }
        if (filter.path() != null && !filter.path().isEmpty()) {
            query.where(hostEntity.path.contains(filter.path()));
        }
        if (filter.createdAtStart() != null && filter.createdAtEnd() != null) {
            query.where(
                hostEntity.createdAt.between(filter.createdAtStart(), filter.createdAtEnd()));
        }
        if (filter.updatedAtStart() != null && filter.updatedAtEnd() != null) {
            query.where(
                hostEntity.updatedAt.between(filter.updatedAtStart(), filter.updatedAtEnd()));
        }

        return query;
    }
}
