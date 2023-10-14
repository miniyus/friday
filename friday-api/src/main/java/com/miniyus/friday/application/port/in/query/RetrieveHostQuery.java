package com.miniyus.friday.application.port.in.query;

import com.miniyus.friday.domain.hosts.*;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.domain.hosts.searches.SearchIds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetrieveHostQuery {
    Host retrieveHostById(HostIds findHostById);

    Host retrieveHost(WhereHost whereHost);

    Page<Host> retrieveHosts(HostFilter filter, Pageable pageable);

    Page<Host> retrieveHostByPublish(
        WherePublish wherePublish,
        Pageable pageable
    );
}
