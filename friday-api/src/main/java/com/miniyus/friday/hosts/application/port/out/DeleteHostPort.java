package com.miniyus.friday.hosts.application.port.out;

public interface DeleteHostPort {
    boolean existsById(Long id);
    void deleteById(Long id);
}
