package com.miniyus.friday.api.hosts.application.port.out;

public interface DeleteHostPort {
    boolean existsById(Long id);
    void deleteById(Long id);
}
