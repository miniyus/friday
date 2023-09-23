package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.adapter.in.rest.request.UpdateHostRequest;
import com.miniyus.friday.adapter.in.rest.resource.HostResource;

public interface UpdateHostUsecase {
    HostResource updateHost(Long id, Long userId, UpdateHostRequest request);
}
