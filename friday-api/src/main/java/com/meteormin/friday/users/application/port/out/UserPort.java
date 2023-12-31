package com.meteormin.friday.users.application.port.out;

import com.meteormin.friday.users.domain.User;
import com.meteormin.friday.users.domain.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserPort {
    User createUser(User user);

    boolean isUniqueEmail(String email);

    void deleteById(Long id);

    User updateUser(User user);

    Optional<User> findById(Long id);

    User resetPassword(User user);

    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    Page<User> findAll(UserFilter searchUser);

}
