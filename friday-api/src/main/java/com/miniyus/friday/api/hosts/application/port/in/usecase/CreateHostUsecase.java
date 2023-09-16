package com.miniyus.friday.api.hosts.application.port.in.usecase;

import com.miniyus.friday.api.hosts.domain.Host;

public interface CreateHostUsecase {
    Host createHost(CreateHostRequest command, Long userId);
}
