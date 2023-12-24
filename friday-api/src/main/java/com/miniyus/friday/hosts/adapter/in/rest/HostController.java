package com.miniyus.friday.hosts.adapter.in.rest;

import com.miniyus.friday.common.hexagon.BaseController;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.hosts.domain.HostIds;
import com.miniyus.friday.hosts.adapter.in.rest.request.CreateHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.request.RetrieveHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.request.UpdateHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.resource.HostResources;
import com.miniyus.friday.hosts.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.hosts.application.port.in.usecase.HostUsecase;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static com.miniyus.friday.hosts.adapter.in.rest.resource.HostResources.*;

@RestAdapter(path = "/v1/hosts")
@RequiredArgsConstructor
public class HostController extends BaseController {
    private final HostUsecase hostUsecase;
    private final RetrieveHostQuery retrieveHostQuery;

    private PrincipalUserInfo getUserInfo() {
        return (PrincipalUserInfo) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> createHost(
        @RequestBody @Valid CreateHostRequest request) {
        var host = hostUsecase.createHost(request.toDomain(getUserInfo().getId()));

        return ResponseEntity.created(createUri("/{id}", host.getId()))
            .body(HostResource.fromDomain(host));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> updateHost(
        @PathVariable Long id,
        @RequestBody @Valid UpdateHostRequest request
    ) {
        var host = hostUsecase.updateHost(request.toDomain(id, getUserInfo().getId()));
        return ResponseEntity.ok(HostResource.fromDomain(host));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public void deleteHost(
        @PathVariable Long id
    ) {
        hostUsecase.deleteHostById(
            HostIds.builder()
                .id(id)
                .userId(getUserInfo().getId())
                .build()
        );
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Page<HostResource>> retrieveHosts(
        @QueryParam @Valid RetrieveHostRequest retrieveAll
    ) {
        RetrieveHostRequest req;
        if (retrieveAll == null) {
            req = RetrieveHostRequest.create();
        } else {
            req = retrieveAll;
        }

        var domains = retrieveHostQuery.retrieveHosts(
            req.toDomain(getUserInfo().getId()),
            req.getPageable()
        );

        return ResponseEntity.ok(
            HostResources.fromDomains(domains)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> retrieveHost(
        @PathVariable Long id
    ) {
        var host = retrieveHostQuery.retrieveHostById(new HostIds(id, getUserInfo().getId()));
        return ResponseEntity.ok(
            HostResource.fromDomain(host)
        );
    }
}
