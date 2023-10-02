package com.miniyus.friday.adapter.in.rest;

import com.miniyus.friday.adapter.in.rest.request.CreateHostRequest;
import com.miniyus.friday.adapter.in.rest.request.UpdateHostRequest;
import com.miniyus.friday.adapter.in.rest.resource.HostResources;
import com.miniyus.friday.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.adapter.in.rest.request.RetrieveHostRequest;
import com.miniyus.friday.adapter.in.rest.resource.HostResources.*;
import com.miniyus.friday.application.port.in.usecase.*;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.domain.hosts.HostIds;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestAdapter(path = "/v1/hosts")
@RequiredArgsConstructor
public class HostController {
    final CreateHostUsecase createHostUsecase;
    final UpdateHostUsecase updateHostUsecase;
    final DeleteHostUsecase deleteHostUsecase;
    final RetrieveHostQuery retrieveHostQuery;

    private PrincipalUserInfo getUserInfo() {
        return (PrincipalUserInfo) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> createHost(
        @RequestBody @Valid CreateHostRequest request) {

        var host = createHostUsecase.createHost(request.toDomain(getUserInfo().getId()));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(host.getId())
            .toUri();

        return ResponseEntity.created(location).body(HostResource.fromDomain(host));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> updateHost(
        @PathVariable Long id,
        @RequestBody @Valid UpdateHostRequest request
    ) {
        var host = updateHostUsecase.updateHost(request.toDomain(id, getUserInfo().getId()));
        return ResponseEntity.ok(HostResource.fromDomain(host));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public void deleteHost(
        @PathVariable Long id
    ) {
        deleteHostUsecase.deleteById(
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

        var domains = retrieveHostQuery.retrieveAll(
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
        var host = retrieveHostQuery.retrieveById(new HostIds(id, getUserInfo().getId()));
        return ResponseEntity.ok(
            HostResource.fromDomain(host)
        );
    }
}
