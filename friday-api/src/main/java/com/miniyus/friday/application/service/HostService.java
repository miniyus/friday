package com.miniyus.friday.application.service;

import com.miniyus.friday.application.exception.HostExistsException;
import com.miniyus.friday.application.exception.SearchNotFoundException;
import com.miniyus.friday.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.application.port.in.usecase.*;
import com.miniyus.friday.application.port.out.*;
import com.miniyus.friday.common.error.ForbiddenErrorException;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.application.exception.HostNotFoundException;
import com.miniyus.friday.domain.hosts.*;
import com.miniyus.friday.domain.hosts.searches.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Usecase
public class HostService
    implements CreateHostUsecase, UpdateHostUsecase, DeleteHostUsecase, RetrieveHostQuery {

    CreateHostPort createHostPort;
    UpdateHostPort updateHostPort;
    RetrieveHostPort retrieveHostPort;
    DeleteHostPort deleteHostPort;

    @Override
    public Host retrieveById(HostIds findHostById) {
        var host = retrieveHostPort.findById(findHostById.id()).orElseThrow(
            HostNotFoundException::new
        );

        if (!host.getUserId().equals(findHostById.userId())) {
            throw new ForbiddenErrorException();
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
    public Page<Host> retrieveAll(HostFilter filter, Pageable pageable) {
        if (filter.isEmpty()) {
            throw new ForbiddenErrorException();
        }

        return retrieveHostPort.findAll(filter, pageable);
    }

    @Override
    public Page<Host> retrieveByPublish(
        WherePublish wherePublish,
        Pageable pageable
    ) {
        return retrieveHostPort.findByPublish(wherePublish, pageable);
    }

    @Override
    public Page<Search> retrieveSearches(SearchFilter filter, Pageable pageable) {
        return retrieveHostPort.findSearchAll(filter, pageable);
    }

    @Override
    public Search retrieveSearch(SearchIds ids) {
        if (!accessibleToSearch(ids)) {
            throw new ForbiddenErrorException();
        }

        return retrieveHostPort.findSearchById(ids.id())
            .orElseThrow(SearchNotFoundException::new);
    }

    @Override
    public Host createHost(CreateHost host) {
        WhereHost whereHost = WhereHost.builder()
            .host(host.host())
            .userId(host.userId())
            .build();

        if (!createHostPort.isUniqueHost(whereHost)) {
            throw new HostExistsException();
        }

        return createHostPort.create(Host.create(host));
    }

    @Override
    public Search createSearch(CreateSearch search) {
        var host = retrieveById(
            HostIds.builder()
                .userId(search.userId())
                .id(search.hostId())
                .build()
        );

        if (host == null) {
            throw new ForbiddenErrorException();
        }

        return createHostPort.createSearch(Search.create(search));
    }

    @Override
    public Host updateHost(UpdateHost updateHost) {
        var exists = updateHostPort.findById(updateHost.ids().id()).orElseThrow(
            HostNotFoundException::new
        );

        if (!exists.getUserId().equals(updateHost.ids().userId())) {
            throw new ForbiddenErrorException();
        }

        exists.update(updateHost);

        return updateHostPort.update(exists);
    }

    @Override
    public Search updateSearch(UpdateSearch updateSearch) {
        if (!accessibleToSearch(updateSearch.ids())) {
            throw new ForbiddenErrorException();
        }

        var exists = updateHostPort.findSearchById(updateSearch.ids().id())
            .orElseThrow(SearchNotFoundException::new);

        exists.update(updateSearch);
        return updateHostPort.updateSearch(exists);
    }

    /**
     * Deletes a record by its ID if the record belongs to the specified user.
     *
     * @param ids host id and user id
     */
    @Override
    public void deleteById(HostIds ids) {
        var deletable = deleteHostPort.findById(ids.id())
            .orElseThrow(HostNotFoundException::new)
            .getUserId().equals(ids.userId());

        if (!deletable) {
            throw new ForbiddenErrorException();
        }

        deleteHostPort.deleteById(ids.id());
    }

    @Override
    public void deleteSearchById(SearchIds ids) {
        if (!accessibleToSearch(ids)) {
            throw new ForbiddenErrorException();
        }

        deleteHostPort.findSearchById(ids.id())
            .orElseThrow(SearchNotFoundException::new);
        deleteHostPort.deleteSearchById(ids.id());
    }

    private boolean accessibleToSearch(SearchIds ids) {
        var host = this.retrieveById(
            HostIds.builder()
                .id(ids.hostId())
                .userId(ids.userId())
                .build()
        );

        return host != null;
    }
}
