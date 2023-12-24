package com.miniyus.friday.hosts.application.service;

import com.miniyus.friday.common.error.ForbiddenErrorException;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.hosts.application.exception.HostExistsException;
import com.miniyus.friday.hosts.application.exception.HostNotFoundException;
import com.miniyus.friday.hosts.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.hosts.application.port.in.usecase.HostUsecase;
import com.miniyus.friday.hosts.application.port.out.HostPort;
import com.miniyus.friday.hosts.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Usecase
public class HostService implements HostUsecase, RetrieveHostQuery {
    private final HostPort hostPort;

    @Override
    public Host retrieveHostById(HostIds findHostById) {
        var host = hostPort.findById(findHostById.id()).orElseThrow(
            HostNotFoundException::new
        );

        if (!host.getUserId().equals(findHostById.userId())) {
            throw new ForbiddenErrorException();
        }

        return host;
    }


    @Override
    public Host retrieveHost(WhereHost whereHost) {
        return hostPort.findByHost(whereHost).orElseThrow(
            HostNotFoundException::new
        );
    }

    @Override
    public Page<Host> retrieveHosts(HostFilter filter, Pageable pageable) {
        if (filter.isEmpty()) {
            throw new ForbiddenErrorException();
        }

        return hostPort.findAll(filter, pageable);
    }

    @Override
    public Page<Host> retrieveHostByPublish(
        WherePublish wherePublish,
        Pageable pageable
    ) {
        return hostPort.findByPublish(wherePublish, pageable);
    }

    @Override
    public Host createHost(CreateHost host) {
        WhereHost whereHost = WhereHost.builder()
            .host(host.host())
            .userId(host.userId())
            .build();

        if (!hostPort.isUniqueHost(whereHost)) {
            throw new HostExistsException();
        }

        return hostPort.create(Host.create(host));
    }

    @Override
    public Host updateHost(UpdateHost updateHost) {
        var exists = hostPort.findById(updateHost.ids().id()).orElseThrow(
            HostNotFoundException::new
        );

        if (!exists.getUserId().equals(updateHost.ids().userId())) {
            throw new ForbiddenErrorException();
        }

        exists.update(updateHost);

        return hostPort.update(exists);
    }

    /**
     * Deletes a record by its ID if the record belongs to the specified user.
     *
     * @param ids host id and user id
     */
    @Override
    public void deleteHostById(HostIds ids) {
        var deletable = hostPort.findById(ids.id())
            .orElseThrow(HostNotFoundException::new)
            .getUserId().equals(ids.userId());

        if (!deletable) {
            throw new ForbiddenErrorException();
        }

        hostPort.deleteById(ids.id());
    }
}
