package com.miniyus.friday.hosts.application.port.out;

import com.miniyus.friday.hosts.domain.Host;

import java.util.Optional;

public interface UpdateHostPort {
    Optional<Host> findById(Long id);
    Host updateHost(Host host);
}
