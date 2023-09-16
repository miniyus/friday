package com.miniyus.friday.api.hosts.application.port.out;

import com.miniyus.friday.api.hosts.domain.Host;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RetrieveHostPort {
    Optional<Host> findById(Long id);

    Optional<Host> findByHost(Host.WhereHost whereHost);

    Page<Host> findByPublish(Host.WherePublish wherePublish, Pageable pageable);

    Page<Host> findAll(Pageable pageable);

    Page<Host> findAll(Host.HostFilter host, Pageable pageable);

}
