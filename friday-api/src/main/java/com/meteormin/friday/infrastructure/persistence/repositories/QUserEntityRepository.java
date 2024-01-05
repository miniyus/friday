package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.infrastructure.persistence.entities.UserEntity;
import com.meteormin.friday.users.domain.UserFilter;
import org.springframework.data.domain.Page;

public interface QUserEntityRepository {
    Page<UserEntity> findUsers(
        UserFilter searchUser);
}
