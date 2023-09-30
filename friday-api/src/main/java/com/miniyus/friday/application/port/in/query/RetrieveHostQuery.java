package com.miniyus.friday.application.port.in.query;

import com.miniyus.friday.domain.hosts.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetrieveHostQuery {
    Host retrieveById(FindHostById findHostById);

    Host retrieveByHost(WhereHost whereHost);

    Page<Host> retrieveAll(HostFilter filter, Pageable pageable);

    Page<Host> retrieveByPublish(
        WherePublish wherePublish,
        Pageable pageable
    );
}
