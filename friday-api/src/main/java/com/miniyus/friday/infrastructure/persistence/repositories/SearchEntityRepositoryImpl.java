package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public Page<SearchEntity> findAllByHostId(Long hostId, Pageable pageable) {
        var countQuery = jpaQueryFactory.select(searchEntity.count());
        var count = countQuery.where(searchEntity.host.id.eq(hostId)).fetchOne();

        var query = jpaQueryFactory.selectFrom(searchEntity);
        var searchEntities = query.where(searchEntity.host.id.eq(hostId)).fetch();

        return new PageImpl<>(
            searchEntities,
            pageable,
            Objects.requireNonNullElse(count, 0L)
        );
    }

    @Override
    public Page<SearchEntity> findSearches(SearchFilter searchFilter, Pageable pageable) {
        var countQuery = jpaQueryFactory.select(searchEntity.count());
        var count = countQuery.where(searchEntity.host.id.eq(searchFilter.hostId())).fetchOne();

        var query = jpaQueryFactory.selectFrom(searchEntity);
        var searchEntities = whereFilter(query, searchFilter).fetch();

        return new PageImpl<>(
            searchEntities,
            pageable,
            Objects.requireNonNullElse(count, 0L)
        );
    }

    private JPAQuery<SearchEntity> whereFilter(JPAQuery<SearchEntity> query, SearchFilter filter) {
        query.where(searchEntity.host.id.eq(filter.hostId()));
        query.where(searchEntity.publish.eq(filter.publish()));

        if(filter.queryKey() != null && !filter.queryKey().isBlank()) {
            query.where(searchEntity.queryKey.contains(filter.queryKey()));
        }

        if(filter.query() != null && !filter.query().isBlank()) {
            query.where(searchEntity.query.contains(filter.query()));
        }

        return query;
    }
}
