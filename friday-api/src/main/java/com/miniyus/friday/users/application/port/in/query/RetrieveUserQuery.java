package com.miniyus.friday.users.application.port.in.query;

import java.util.List;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
public interface RetrieveUserQuery {
    List<User> findAll();

    Page<User> findAll(UserFilter request, Pageable pageable);

    User findById(Long id);
}
