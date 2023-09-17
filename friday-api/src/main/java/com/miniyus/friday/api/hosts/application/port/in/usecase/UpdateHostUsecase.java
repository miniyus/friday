package com.miniyus.friday.api.hosts.application.port.in.usecase;

import com.miniyus.friday.api.hosts.application.port.in.HostResource;

public interface UpdateHostUsecase {
    HostResource updateHost(Long id, Long userId, UpdateHostRequest request);
}
