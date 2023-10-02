package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.searches.Search;

import java.util.Optional;

public interface UpdateHostPort {
    Optional<Host> findById(Long id);
    Host update(Host host);

    Optional<Search> findSearchById(Long id);

    Search updateSearch(Search search);
}
