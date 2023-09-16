package com.miniyus.friday.api.users.application.port.in.query;

import java.util.Collection;

import com.miniyus.friday.api.users.domain.User;
import org.springframework.data.domain.Page;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
public interface RetrieveUserQuery {
    Collection<User> findAll();

    Page<User> findAll(RetrieveUserRequest request);

    User findById(Long id);
}
