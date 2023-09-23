package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.hosts.Host;

public interface CreateHostUsecase {
    Host createHost(Host host);
}
