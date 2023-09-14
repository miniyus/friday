package com.miniyus.friday.hosts.application.port.in.usecase;

import com.miniyus.friday.hosts.domain.Host;

public interface CreateHostUsecase {
    Host createHost(CreateHostCommand command);
}
