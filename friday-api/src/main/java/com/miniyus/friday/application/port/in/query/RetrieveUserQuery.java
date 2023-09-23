package com.miniyus.friday.application.port.in.query;

import java.util.List;
import com.miniyus.friday.domain.users.User;
import com.miniyus.friday.domain.users.UserFilter;
import org.springframework.data.domain.Page;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
public interface RetrieveUserQuery {
    List<User> findAll();

    Page<User> findAll(UserFilter request);

    User findById(Long id);
}