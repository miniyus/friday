package com.meteormin.friday.hosts.application.port.out;

import com.meteormin.friday.hosts.domain.Host;
import com.meteormin.friday.hosts.domain.HostFilter;
import com.meteormin.friday.hosts.domain.WhereHost;
import com.meteormin.friday.hosts.domain.WherePublish;
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

    Page<Host> findAll(HostFilter host);

    void deleteById(Long id);

}
