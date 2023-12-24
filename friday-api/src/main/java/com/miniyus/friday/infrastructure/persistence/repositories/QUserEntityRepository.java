package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.users.domain.UserFilter;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QUserEntityRepository {
    Page<UserEntity> findUsers(
        UserFilter searchUser,
        Pageable pageable);
}
