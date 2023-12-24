package com.miniyus.friday.hosts.application.port.in.query;

import com.miniyus.friday.hosts.domain.*;
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
