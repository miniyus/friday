package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.UpdateHost;

public interface UpdateHostUsecase {
    Host updateHost(UpdateHost updateHost);
}
