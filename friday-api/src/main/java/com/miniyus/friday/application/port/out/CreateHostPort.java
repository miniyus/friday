package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.Host;

public interface CreateHostPort {
    Host create(Host host);

    boolean isUniqueHost(Host.WhereHost whereHost);
}
