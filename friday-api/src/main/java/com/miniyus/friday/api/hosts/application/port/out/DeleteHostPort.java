package com.miniyus.friday.api.hosts.application.port.out;

import com.miniyus.friday.api.hosts.domain.Host;

import java.util.Optional;

public interface DeleteHostPort {
    Optional<Host> findById(Long id);

    void deleteById(Long id);
}
