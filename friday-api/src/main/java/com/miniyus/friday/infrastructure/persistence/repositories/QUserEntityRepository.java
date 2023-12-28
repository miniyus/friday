package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.users.domain.UserFilter;
import org.springframework.data.domain.Page;

public interface QUserEntityRepository {
    Page<UserEntity> findUsers(
        UserFilter searchUser);
}
