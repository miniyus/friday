package com.miniyus.friday.api.hosts.application.service;

import com.miniyus.friday.api.hosts.application.exception.HostExistsException;
import com.miniyus.friday.api.hosts.application.port.in.query.RetrieveHostCommand;
import com.miniyus.friday.api.hosts.application.port.in.query.RetrieveHostQuery;
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
    public Host retrieveById(Long id) {
        return retrieveHostPort.findById(id).orElseThrow(
            HostNotFoundException::new
        );
    }

    @Override
    public Host retrieveByHost(String host) {
        return retrieveHostPort.findByHost(host).orElseThrow(
            HostNotFoundException::new
        );
    }

    @Override
    public Page<Host> retrieveAll(RetrieveHostCommand command) {
        var filter = Host.HostFilter.builder()
            .summary(command.summary())
            .path(command.host())
            .description(command.description())
            .createdAtStart(command.createdAtStart())
            .createdAtEnd(command.createdAtEnd())
            .updatedAtStart(command.updatedAtStart())
            .updatedAtEnd(command.updatedAtEnd())
            .build();

        if (filter.isEmpty()) {
            return retrieveHostPort.findAll(command.pageable());
        }

        return retrieveHostPort.findAll(filter, command.pageable());
    }

    @Override
    public Page<Host> retrieveByPublish(boolean isPublish, Pageable pageable) {
        return retrieveHostPort.findByPublish(isPublish, pageable);
    }

    @Override
    public Host createHost(CreateHostCommand command) {
        if (!createHostPort.isUniqueHost(command.host())) {
            throw new HostExistsException();
        }

        var host = Host.builder()
            .host(command.host())
            .summary(command.summary())
            .description(command.description())
            .path(command.path())
            .publish(command.publish())
            .build();

        return createHostPort.create(host);
    }

    @Override
    public Host updateHost(UpdateHostCommand command) {
        var host = updateHostPort.findById(command.id()).orElseThrow(
            HostNotFoundException::new
        );

        host.update(
            command.host(),
            command.summary(),
            command.description(),
            command.path(),
            command.publish()
        );

        return updateHostPort.update(host);
    }

    @Override
    public void deleteById(Long id) {
        if(deleteHostPort.existsById(id)) {
            deleteHostPort.deleteById(id);
        }

        throw new HostNotFoundException();
    }
}
