package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.Host;

import java.util.Optional;

public interface DeleteHostPort {
    Optional<Host> findById(Long id);

    void deleteById(Long id);
}
