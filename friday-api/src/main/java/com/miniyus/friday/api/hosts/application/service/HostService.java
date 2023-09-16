package com.miniyus.friday.api.hosts.application.service;

import com.miniyus.friday.api.hosts.application.exception.HostExistsException;
import com.miniyus.friday.api.hosts.application.exception.HostForbiddenException;
import com.miniyus.friday.api.hosts.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.api.hosts.application.port.in.query.RetrieveHostRequest;
import com.miniyus.friday.api.hosts.application.port.in.usecase.*;
import com.miniyus.friday.api.hosts.application.port.out.CreateHostPort;
import com.miniyus.friday.api.hosts.application.port.out.DeleteHostPort;
import com.miniyus.friday.api.hosts.application.port.out.RetrieveHostPort;
import com.miniyus.friday.api.hosts.application.port.out.UpdateHostPort;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.api.hosts.application.exception.HostNotFoundException;
import com.miniyus.friday.api.hosts.domain.Host;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Usecase
public class HostService implements CreateHostUsecase, UpdateHostUsecase, DeleteHostUsecase,
    RetrieveHostQuery {

    CreateHostPort createHostPort;
    UpdateHostPort updateHostPort;
    RetrieveHostPort retrieveHostPort;
    DeleteHostPort deleteHostPort;

    @Override
    public Host retrieveById(Long id, Long userId) {
        var host =retrieveHostPort.findById(id).orElseThrow(
            HostNotFoundException::new
        );

        if(!host.getUserId().equals(userId)) {
            throw new HostForbiddenException();
        }

        return host;
    }

    @Override
    public Host retrieveByHost(RetrieveHostRequest.RetrieveHost retrieveHost, Long userId) {
        var whereHost = Host.WhereHost.builder()
            .host(retrieveHost.host())
            .userId(userId)
            .build();

        return retrieveHostPort.findByHost(whereHost).orElseThrow(
            HostNotFoundException::new
        );
    }

    @Override
    public Page<Host> retrieveAll(RetrieveHostRequest.RetrieveAll req, Long userId) {
        var filter = Host.HostFilter.builder()
            .summary(req.getSummary())
            .path(req.getHost())
            .description(req.getDescription())
            .createdAtStart(req.getCreatedAtStart())
            .createdAtEnd(req.getCreatedAtEnd())
            .updatedAtStart(req.getUpdatedAtStart())
            .updatedAtEnd(req.getUpdatedAtEnd())
            .userId(userId)
            .build();

        if (filter.isEmpty()) {
            return retrieveHostPort.findAll(req.getPageable());
        }

        return retrieveHostPort.findAll(filter, req.getPageable());
    }

    @Override
    public Page<Host> retrieveByPublish(
        RetrieveHostRequest.RetrievePublish retrievePublish,
        Pageable pageable,
        Long userId
    ) {
        var wherePublish = Host.WherePublish.builder()
            .publish(retrievePublish.publish())
            .userId(userId)
            .build();

        return retrieveHostPort.findByPublish(wherePublish, pageable);
    }

    @Override
    public Host createHost(CreateHostRequest request, Long userId) {
        var whereHost = Host.WhereHost.builder()
            .host(request.host())
            .userId(userId)
            .build();

        if (!createHostPort.isUniqueHost(whereHost)) {
            throw new HostExistsException();
        }

        var host = Host.builder()
            .host(request.host())
            .summary(request.summary())
            .description(request.description())
            .path(request.path())
            .publish(request.publish())
            .build();

        return createHostPort.create(host);
    }

    @Override
    public Host updateHost(
        Long id,
        Long userId,
        UpdateHostRequest request) {

        var host = updateHostPort.findById(id).orElseThrow(
            HostNotFoundException::new
        );

        if (!host.getUserId().equals(userId)) {
            throw new HostForbiddenException();
        }

        host.update(
            request.host(),
            request.summary(),
            request.description(),
            request.path(),
            request.publish()
        );

        return updateHostPort.update(host);
    }

    @Override
    public void deleteById(Long id, Long userId) {
        var deletable = deleteHostPort.findById(id)
            .orElseThrow(HostNotFoundException::new)
            .getUserId()
            .equals(userId);

        if (deletable) {
            deleteHostPort.deleteById(id);
        }

        throw new HostForbiddenException();
    }
}
