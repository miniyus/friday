package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.HostFilter;
import com.miniyus.friday.domain.hosts.WhereHost;
import com.miniyus.friday.domain.hosts.WherePublish;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.domain.hosts.searches.WhereSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HostPort {
    Host create(Host host);

    boolean isUniqueHost(WhereHost whereHost);

    Host update(Host host);

    Optional<Host> findById(Long id);

    Optional<Host> findByHost(WhereHost whereHost);

    Page<Host> findByPublish(WherePublish wherePublish, Pageable pageable);

    Page<Host> findAll(Pageable pageable);

    Page<Host> findAll(HostFilter host, Pageable pageable);

    void deleteById(Long id);

}
