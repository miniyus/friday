package com.miniyus.friday.hosts.application.port.out;

import com.miniyus.friday.hosts.domain.Host;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RetrieveHostPort {
    Optional<Host> findById(Long id);

    Optional<Host> findByHost(String host);

    Page<Host> findByPublish(boolean isPublish, Pageable pageable);

    Page<Host> findAll(Pageable pageable);

    Page<Host> findAll(Host.HostFilter host, Pageable pageable);

}
