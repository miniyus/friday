package com.miniyus.friday.users.application.port.out;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.User.SearchUser;

/**
 * 
 * @author miniyus
 * @date 2023/09/06
 */
public interface RetrieveUserPort {
    Collection<User> findAll();

    Page<User> findAll(Pageable pageable);

    Page<User> findAll(SearchUser searchUser, Pageable pageable);

    User findById(Long id);
}
