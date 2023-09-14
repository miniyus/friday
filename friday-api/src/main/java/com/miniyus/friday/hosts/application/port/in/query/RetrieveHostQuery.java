package com.miniyus.friday.hosts.application.port.in.query;

import com.miniyus.friday.hosts.domain.Host;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RetrieveHostQuery {
    Host retrieveById(Long id);
    Host retrieveByHost(String host);

    Page<Host> retrieveAll(RetrieveHostCommand filter);

    Page<Host> retrieveByPublish(boolean isPublish, Pageable pageable);
}
