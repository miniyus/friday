package com.meteormin.friday.hosts.application.port.in.usecase;

import com.meteormin.friday.hosts.domain.CreateHost;
import com.meteormin.friday.hosts.domain.Host;
import com.meteormin.friday.hosts.domain.HostIds;
import com.meteormin.friday.hosts.domain.PatchHost;

public interface HostUsecase {
    Host createHost(CreateHost host);
    Host patchHost(PatchHost patchHost);

    void deleteHostById(HostIds ids);
}
