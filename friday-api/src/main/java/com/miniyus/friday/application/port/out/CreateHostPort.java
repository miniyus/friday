package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.WhereHost;

public interface CreateHostPort {
    Host create(Host host);

    boolean isUniqueHost(WhereHost whereHost);
}
