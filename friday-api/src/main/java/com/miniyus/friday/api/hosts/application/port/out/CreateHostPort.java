package com.miniyus.friday.api.hosts.application.port.out;

import com.miniyus.friday.api.hosts.domain.Host;

public interface CreateHostPort {
    Host create(Host host);

    boolean isUniqueHost(Host.WhereHost whereHost);
}
