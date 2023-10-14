package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.hosts.CreateHost;
import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.HostIds;
import com.miniyus.friday.domain.hosts.UpdateHost;

public interface HostUsecase {
    Host createHost(CreateHost host);
    Host updateHost(UpdateHost updateHost);

    void deleteHostById(HostIds ids);
}
