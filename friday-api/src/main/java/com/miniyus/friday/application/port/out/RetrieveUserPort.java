package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.users.User;
import com.miniyus.friday.domain.users.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
/**
 * 
 * @author miniyus
 * @date 2023/09/06
 */
public interface RetrieveUserPort {
    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    Page<User> findAll(UserFilter searchUser, Pageable pageable);

    Optional<User> findById(Long id);
}
