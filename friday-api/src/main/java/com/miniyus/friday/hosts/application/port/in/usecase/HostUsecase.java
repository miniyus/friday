package com.miniyus.friday.hosts.application.port.in.usecase;

import com.miniyus.friday.hosts.domain.CreateHost;
import com.miniyus.friday.hosts.domain.Host;
import com.miniyus.friday.hosts.domain.HostIds;
import com.miniyus.friday.hosts.domain.UpdateHost;

public interface HostUsecase {
    Host createHost(CreateHost host);
    Host updateHost(UpdateHost updateHost);

    void deleteHostById(HostIds ids);
}
