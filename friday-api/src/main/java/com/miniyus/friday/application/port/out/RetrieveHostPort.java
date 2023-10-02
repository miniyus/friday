package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.*;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.domain.hosts.searches.SearchIds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RetrieveHostPort {
    Optional<Host> findById(Long id);

    Optional<Host> findByHost(WhereHost whereHost);

    Page<Host> findByPublish(WherePublish wherePublish, Pageable pageable);

    Page<Host> findAll(Pageable pageable);

    Page<Host> findAll(HostFilter host, Pageable pageable);

    Optional<Search> findSearchById(Long id);

    Page<Search> findSearchAll(Long hostId, Pageable pageable);

    Page<Search> findSearchAll(SearchFilter searchFilter, Pageable pageable);
}
