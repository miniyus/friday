package com.miniyus.friday.api.users.application.port.in.query;

import java.util.Collection;

import com.miniyus.friday.api.users.application.port.in.UserResource;
import com.miniyus.friday.api.users.domain.User;
import org.springframework.data.domain.Page;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
public interface RetrieveUserQuery {
    Collection<UserResource> findAll();

    Page<UserResource> findAll(RetrieveUserRequest request);

    UserResource findById(Long id);
}
