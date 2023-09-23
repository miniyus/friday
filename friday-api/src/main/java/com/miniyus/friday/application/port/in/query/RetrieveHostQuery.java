package com.miniyus.friday.application.port.in.query;

import com.miniyus.friday.adapter.in.rest.request.RetrieveHostRequest;
import com.miniyus.friday.adapter.in.rest.resource.HostResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetrieveHostQuery {
    HostResource retrieveById(Long id, Long userId);

    HostResource retrieveByHost(
        RetrieveHostRequest.RetrieveHost retrieveHost,
        Long userId
    );

    Page<HostResource> retrieveAll(RetrieveHostRequest.RetrieveAll filter, Long userId);

    Page<HostResource> retrieveByPublish(
        RetrieveHostRequest.RetrievePublish retrievePublish,
        Pageable pageable,
        Long userId
    );
}
