package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.HostFilter;
import com.miniyus.friday.domain.hosts.WhereHost;
import com.miniyus.friday.domain.hosts.WherePublish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RetrieveHostPort {
    Optional<Host> findById(Long id);

    Optional<Host> findByHost(WhereHost whereHost);

    Page<Host> findByPublish(WherePublish wherePublish, Pageable pageable);

    Page<Host> findAll(Pageable pageable);

    Page<Host> findAll(HostFilter host, Pageable pageable);

}
