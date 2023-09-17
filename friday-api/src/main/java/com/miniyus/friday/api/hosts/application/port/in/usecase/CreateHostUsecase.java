package com.miniyus.friday.api.hosts.application.port.in.usecase;

import com.miniyus.friday.api.hosts.application.port.in.HostResource;

public interface CreateHostUsecase {
    HostResource createHost(CreateHostRequest command, Long userId);
}
