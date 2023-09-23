package com.miniyus.friday.application;

import com.miniyus.friday.application.exception.HostExistsException;
import com.miniyus.friday.application.exception.HostForbiddenException;
import com.miniyus.friday.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.application.port.in.usecase.*;
import com.miniyus.friday.application.port.out.CreateHostPort;
import com.miniyus.friday.application.port.out.DeleteHostPort;
import com.miniyus.friday.application.port.out.RetrieveHostPort;
import com.miniyus.friday.application.port.out.UpdateHostPort;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.application.exception.HostNotFoundException;
import com.miniyus.friday.domain.hosts.*;
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
    public Host retrieveById(FindHostById findHostById) {
        var host = retrieveHostPort.findById(findHostById.id()).orElseThrow(
            HostNotFoundException::new
        );

        if (!host.getUserId().equals(findHostById.userId())) {
            throw new HostForbiddenException();
        }

        return host;
    }


    @Override
    public Host retrieveByHost(WhereHost whereHost) {
        return retrieveHostPort.findByHost(whereHost).orElseThrow(
            HostNotFoundException::new
        );
    }

    @Override
    public Page<Host> retrieveAll(HostFilter filter) {

        if (filter.isEmpty()) {
            return retrieveHostPort.findAll(filter.pageable());
        }

        return retrieveHostPort.findAll(filter, filter.pageable());
    }

    @Override
    public Page<Host> retrieveByPublish(
        WherePublish wherePublish,
        Pageable pageable
    ) {
        return retrieveHostPort.findByPublish(wherePublish, pageable);
    }

    @Override
    public Host createHost(Host host) {
        WhereHost whereHost = WhereHost.builder()
            .host(host.getHost())
            .userId(host.getUserId())
            .build();

        if (!createHostPort.isUniqueHost(whereHost)) {
            throw new HostExistsException();
        }

        return createHostPort.create(host);
    }

    @Override
    public Host updateHost(UpdateHost updateHost) {

        var exists = updateHostPort.findById(updateHost.id()).orElseThrow(
            HostNotFoundException::new
        );

        if (!exists.getUserId().equals(updateHost.userId())) {
            throw new HostForbiddenException();
        }

        exists.update(
            updateHost.host(),
            updateHost.summary(),
            updateHost.description(),
            updateHost.path(),
            updateHost.publish()
        );

        return updateHostPort.update(exists);
    }

    /**
     * Deletes a record by its ID if the record belongs to the specified user.
     *
     * @param id     the ID of the record to be deleted
     * @param userId the ID of the user
     */
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
