package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.Host;

import java.util.Optional;

public interface UpdateHostPort {
    Optional<Host> findById(Long id);
    Host update(Host host);
}
