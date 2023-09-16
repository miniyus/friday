package com.miniyus.friday.api.users.application.port.out;

import java.util.Collection;
import java.util.Optional;

import com.miniyus.friday.api.users.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 
 * @author miniyus
 * @date 2023/09/06
 */
public interface RetrieveUserPort {
    Collection<User> findAll();

    Page<User> findAll(Pageable pageable);

    Page<User> findAll(User.SearchUser searchUser, Pageable pageable);

    Optional<User> findById(Long id);
}
