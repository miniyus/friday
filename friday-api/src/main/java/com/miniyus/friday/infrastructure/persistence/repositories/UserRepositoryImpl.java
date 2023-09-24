package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.domain.users.UserFilter;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import static com.miniyus.friday.infrastructure.persistence.entities.QUserEntity.userEntity;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements QUserRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserEntity> findAll(UserFilter searchUser,
        Pageable pageable) {

        var content = whereSearchUser(
            queryFactory.selectFrom(userEntity),
            searchUser)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
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
