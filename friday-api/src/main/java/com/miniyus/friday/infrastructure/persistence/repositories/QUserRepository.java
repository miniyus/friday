package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.domain.users.User;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QUserRepository {
    Page<UserEntity> findAll(
        User.SearchUser searchUser,
        Pageable pageable);
}
