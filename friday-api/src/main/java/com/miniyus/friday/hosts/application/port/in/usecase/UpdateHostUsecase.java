package com.miniyus.friday.hosts.application.port.in.usecase;

import com.miniyus.friday.hosts.domain.Host;

public interface UpdateHostUsecase {
    Host updateHost(UpdateHostCommand command);
}
