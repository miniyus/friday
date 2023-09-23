package com.miniyus.friday.application.port.in.query;

import java.util.Collection;

import com.miniyus.friday.adapter.in.rest.request.RetrieveUserRequest;
import com.miniyus.friday.adapter.in.rest.resource.UserResource;
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
