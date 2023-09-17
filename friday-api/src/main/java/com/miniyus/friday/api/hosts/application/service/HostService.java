package com.miniyus.friday.api.hosts.application.service;

import com.miniyus.friday.api.hosts.application.exception.HostExistsException;
import com.miniyus.friday.api.hosts.application.exception.HostForbiddenException;
import com.miniyus.friday.api.hosts.application.port.in.HostResource;
import com.miniyus.friday.api.hosts.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.api.hosts.application.port.in.query.RetrieveHostRequest;
import com.miniyus.friday.api.hosts.application.port.in.usecase.*;
import com.miniyus.friday.api.hosts.application.port.out.*;
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

    /**
     * Retrieves a host resource by ID for a specific user.
     *
     * @param id     the ID of the host resource
     * @param userId the ID of the user
     * @return the retrieved host resource
     */
    @Override
    public HostResource retrieveById(Long id, Long userId) {
        var host = retrieveHostPort.findById(id).orElseThrow(
            HostNotFoundException::new
        );

        if (!host.getUserId().equals(userId)) {
            throw new HostForbiddenException();
        }

        return HostResource.fromDomain(host);
    }

    /**
     * Retrieves a HostResource by host and user ID.
     *
     * @param retrieveHost the RetrieveHost object containing the host information
     * @param userId       the ID of the user
     * @return the retrieved HostResource
     */
    @Override
    public HostResource retrieveByHost(RetrieveHostRequest.RetrieveHost retrieveHost, Long userId) {
        var whereHost = Host.WhereHost.builder()
            .host(retrieveHost.host())
            .userId(userId)
            .build();

        return HostResource.fromDomain(
            retrieveHostPort.findByHost(whereHost).orElseThrow(
                HostNotFoundException::new
            )
        );
    }

    /**
     * Retrieves all host resources based on the given request parameters.
     *
     * @param req    the RetrieveAll request object containing the filter parameters for the host
     *               resources
     * @param userId the ID of the user performing the retrieval
     * @return a Page object containing the retrieved HostResource objects
     */
    @Override
    public Page<HostResource> retrieveAll(RetrieveHostRequest.RetrieveAll req, Long userId) {
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
            return retrieveHostPort.findAll(req.getPageable())
                .map(HostResource::fromDomain);
        }

        return retrieveHostPort.findAll(filter, req.getPageable())
            .map(HostResource::fromDomain);
    }

    /**
     * Retrieves a page of HostResources based on the specified RetrievePublish, Pageable, and
     * userId.
     *
     * @param retrievePublish the RetrievePublish object specifying the publish value
     * @param pageable        the Pageable object specifying the page number and size
     * @param userId          the ID of the user
     * @return a Page of HostResources
     */
    @Override
    public Page<HostResource> retrieveByPublish(
        RetrieveHostRequest.RetrievePublish retrievePublish,
        Pageable pageable,
        Long userId
    ) {
        var wherePublish = Host.WherePublish.builder()
            .publish(retrievePublish.publish())
            .userId(userId)
            .build();

        return retrieveHostPort.findByPublish(wherePublish, pageable)
            .map(HostResource::fromDomain);
    }

    /**
     * Creates a new host resource.
     *
     * @param request The request object containing the host details.
     * @param userId  The ID of the user creating the host.
     * @return The newly created host resource.
     */
    @Override
    public HostResource createHost(CreateHostRequest request, Long userId) {
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

        return HostResource.fromDomain(createHostPort.create(host));
    }

    /**
     * Updates a host resource based on the provided ID, user ID, and update request.
     *
     * @param id      the ID of the host resource to update
     * @param userId  the ID of the user performing the update
     * @param request the update request containing the new host details
     * @return the updated host resource
     */
    @Override
    public HostResource updateHost(
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

        return HostResource.fromDomain(updateHostPort.update(host));
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
