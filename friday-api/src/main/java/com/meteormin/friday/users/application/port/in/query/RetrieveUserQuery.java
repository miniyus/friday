package com.meteormin.friday.users.application.port.in.query;

import com.meteormin.friday.users.domain.User;
import com.meteormin.friday.users.domain.UserFilter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * [description]
 *
 * @author meteormin
 * @since 2023/09/06
 */
public interface RetrieveUserQuery {
    List<User> findAll();

    Page<User> findAll(UserFilter request);

    User findById(Long id);
}
