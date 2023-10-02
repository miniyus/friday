package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.HostIds;
import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchIds;

import java.util.Optional;

public interface DeleteHostPort {
    Optional<Host> findById(Long id);

    void deleteById(Long id);

    Optional<Search> findSearchById(Long id);

    void deleteSearchById(Long id);
}
