package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.hosts.domain.HostFilter;
import com.meteormin.friday.infrastructure.persistence.SortQueryDsl;
import com.meteormin.friday.infrastructure.persistence.entities.HostEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.meteormin.friday.infrastructure.persistence.entities.QHostEntity.hostEntity;
import static com.meteormin.friday.infrastructure.persistence.entities.QUserEntity.userEntity;

@AllArgsConstructor
@Repository
public class HostEntityRepositoryImpl implements QHostEntityRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<HostEntity> findHosts(HostFilter hostFilter) {
        var query = jpaQueryFactory.selectFrom(hostEntity);
        var pageable = hostFilter.pageable();
        var order = SortQueryDsl.createOrderSpecifier(
            hostEntity,
            pageable.getSort());

        var hosts = whereHostFilter(query, hostFilter)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(order)
            .fetch();

        var count = whereHostFilter(
            jpaQueryFactory.select(hostEntity.count()),
            hostFilter).fetchOne();

        return new PageImpl<>(hosts, pageable, Objects.requireNonNullElse(count, 0L));
    }

    private <T> JPAQuery<T> whereHostFilter(JPAQuery<T> query, HostFilter filter) {
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
