package com.miniyus.friday.api.hosts.application.port.in.query;

import com.miniyus.friday.api.hosts.domain.Host;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetrieveHostQuery {
    Host retrieveById(Long id, Long userId);

    Host retrieveByHost(
        RetrieveHostRequest.RetrieveHost retrieveHost,
        Long userId
    );

    Page<Host> retrieveAll(RetrieveHostRequest.RetrieveAll filter, Long userId);

    Page<Host> retrieveByPublish(
        RetrieveHostRequest.RetrievePublish retrievePublish,
        Pageable pageable,
        Long userId
    );
}
