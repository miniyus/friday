package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.infrastructure.persistence.SortQueryDsl;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.users.domain.UserFilter;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.miniyus.friday.infrastructure.persistence.entities.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserEntityRepositoryImpl implements QUserEntityRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserEntity> findUsers(UserFilter searchUser) {
        var pageable = searchUser.pageable();
        var order = SortQueryDsl.createOrderSpecifier(userEntity, pageable.getSort());
        var content = whereSearchUser(
            queryFactory.selectFrom(userEntity),
            searchUser)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(order)
            .fetch();

        var count = whereSearchUser(queryFactory.select(userEntity.count())
            .from(userEntity), searchUser)
            .fetchOne();

        return new PageImpl<>(content, pageable, Objects.requireNonNullElse(count, 0L));

    }

    private <T> JPAQuery<T> whereSearchUser(
        JPAQuery<T> query,
        UserFilter searchUser
    ) {
        if (searchUser.email() != null && !searchUser.email().isEmpty()) {
            query.where(userEntity.email.contains(searchUser.email()));
        }
        if (searchUser.name() != null && !searchUser.name().isEmpty()) {
            query.where(userEntity.name.contains(searchUser.name()));
        }
        if (searchUser.createdAtStart() != null && searchUser.createdAtEnd() != null) {
            query.where(userEntity.createdAt.between(
                searchUser.createdAtStart(),
                searchUser.createdAtEnd())
            );
        }
        if (searchUser.updatedAtStart() != null && searchUser.updatedAtEnd() != null) {
            query.where(userEntity.updatedAt.between(
                searchUser.updatedAtStart(),
                searchUser.updatedAtEnd())
            );
        }

        return query;
    }
}
