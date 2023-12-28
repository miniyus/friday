package com.miniyus.friday.hosts.application.port.in.usecase;

import com.miniyus.friday.hosts.domain.CreateHost;
import com.miniyus.friday.hosts.domain.Host;
import com.miniyus.friday.hosts.domain.HostIds;
import com.miniyus.friday.hosts.domain.PatchHost;

public interface HostUsecase {
    Host createHost(CreateHost host);
    Host patchHost(PatchHost patchHost);

    void deleteHostById(HostIds ids);
}
