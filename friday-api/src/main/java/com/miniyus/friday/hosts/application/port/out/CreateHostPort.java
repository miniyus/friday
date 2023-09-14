package com.miniyus.friday.hosts.application.port.out;

import com.miniyus.friday.hosts.domain.Host;

public interface CreateHostPort {
    Host create(Host host);

    boolean isUniqueHost(String host);
}
