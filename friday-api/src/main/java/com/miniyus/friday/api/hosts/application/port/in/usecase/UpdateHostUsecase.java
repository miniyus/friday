package com.miniyus.friday.api.hosts.application.port.in.usecase;

import com.miniyus.friday.api.hosts.domain.Host;

public interface UpdateHostUsecase {
    Host updateHost(Long id, Long userId, UpdateHostRequest request);
}
