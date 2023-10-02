package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.searches.Search;

import java.util.Optional;

public interface UpdateSearchPort {
    Search update(Search search);

    Optional<Search> findById(Long id);
}
