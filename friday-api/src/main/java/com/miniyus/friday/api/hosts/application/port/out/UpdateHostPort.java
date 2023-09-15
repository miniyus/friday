package com.miniyus.friday.api.hosts.application.port.out;

import com.miniyus.friday.api.hosts.domain.Host;

import java.util.Optional;

public interface UpdateHostPort {
    Optional<Host> findById(Long id);
    Host update(Host host);
}
