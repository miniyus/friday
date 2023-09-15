package com.miniyus.friday.api.hosts.application.port.in.query;

import com.miniyus.friday.api.hosts.domain.Host;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetrieveHostQuery {
    Host retrieveById(Long id);

    Host retrieveByHost(String host);

    Page<Host> retrieveAll(RetrieveHostCommand filter);

    Page<Host> retrieveByPublish(boolean isPublish, Pageable pageable);
}
