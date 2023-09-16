package com.miniyus.friday.api.hosts.adapter.in.rest;

import com.miniyus.friday.api.hosts.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.api.hosts.application.port.in.query.RetrieveHostRequest;
import com.miniyus.friday.api.hosts.application.port.in.usecase.*;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

        var host = createHostUsecase.createHost(request, getUserInfo().getId());
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
        var host = updateHostUsecase.updateHost(id, getUserInfo().getId(), request);

        return ResponseEntity.ok(HostResource.fromDomain(host));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public void deleteHost(
        @PathVariable Long id
    ) {
        deleteHostUsecase.deleteById(id, getUserInfo().getId());
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Page<HostResource>> retrieveHosts(
        @QueryParam @Valid RetrieveHostRequest.RetrieveAll retrieveAll
    ) {
        RetrieveHostRequest.RetrieveAll req;
        if (retrieveAll == null) {
            req = RetrieveHostRequest.RetrieveAll.builder()
                .pageable(PageRequest.of(0, 20, Sort.Direction.DESC, "createdAt"))
                .build();
        } else {
            req = retrieveAll;
        }

        var hosts = retrieveHostQuery.retrieveAll(req, getUserInfo().getId());
        var response = hosts.map(HostResource::fromDomain);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> retrieveHost(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(
            HostResource.fromDomain(
                retrieveHostQuery.retrieveById(id, getUserInfo().getId())
            )
        );
    }
}
