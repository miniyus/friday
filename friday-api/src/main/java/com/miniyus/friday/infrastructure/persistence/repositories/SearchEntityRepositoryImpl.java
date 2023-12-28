package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.hosts.domain.searches.SearchFilter;
import com.miniyus.friday.infrastructure.persistence.SortQueryDsl;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

import static com.miniyus.friday.infrastructure.persistence.entities.QHostEntity.hostEntity;
import static com.miniyus.friday.infrastructure.persistence.entities.QSearchEntity.searchEntity;

@Repository
@RequiredArgsConstructor
public class SearchEntityRepositoryImpl implements QSearchEntityRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<SearchEntity> findLastSearchByHostId(Long hostId) {
        var query = jpaQueryFactory.selectFrom(searchEntity);
        return Optional.ofNullable(
            query.join(searchEntity.host, hostEntity)
                .where(hostEntity.id.eq(hostId))
                .orderBy(searchEntity.id.desc())
                .fetchOne()
        );
    }

    @Override
    public Page<SearchEntity> findSearches(SearchFilter searchFilter) {
        var pageable = searchFilter.pageable();
        var countQuery = jpaQueryFactory.select(searchEntity.count());
        var count = countQuery.where(searchEntity.host.id.eq(searchFilter.hostId()))
            .fetchOne();

        var query = jpaQueryFactory.selectFrom(searchEntity);
        var order = SortQueryDsl.createOrderSpecifier(
            searchEntity,
            pageable.getSort());

        var searchEntities = whereFilter(query, searchFilter)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(order)
            .fetch();

        return new PageImpl<>(
            searchEntities,
            pageable,
            Objects.requireNonNullElse(count, 0L)
        );
    }

    private JPAQuery<SearchEntity> whereFilter(JPAQuery<SearchEntity> query, SearchFilter filter) {
        query.where(searchEntity.host.id.eq(filter.hostId()));

        if (filter.publish() != null) {
            query.where(searchEntity.publish.eq(filter.publish()));
        }

        if (filter.queryKey() != null && !filter.queryKey().isBlank()) {
            query.where(searchEntity.queryKey.contains(filter.queryKey()));
        }

        if (filter.query() != null && !filter.query().isBlank()) {
            query.where(searchEntity.query.contains(filter.query()));
        }

        return query;
    }
}
