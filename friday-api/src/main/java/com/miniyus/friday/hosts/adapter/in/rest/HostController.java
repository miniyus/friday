package com.miniyus.friday.hosts.adapter.in.rest;

import com.miniyus.friday.api.hosts.HostApi;
import com.miniyus.friday.common.hexagon.BaseController;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.hosts.adapter.in.rest.request.CreateHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.request.RetrieveHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.request.UpdateHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.resource.HostResources;
import com.miniyus.friday.hosts.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.hosts.application.port.in.usecase.HostUsecase;
import com.miniyus.friday.hosts.domain.HostIds;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.annotation.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.miniyus.friday.hosts.adapter.in.rest.resource.HostResources.HostResource;

@RestAdapter(path = "/v1/hosts")
@RequiredArgsConstructor
public class HostController extends BaseController implements HostApi {
    private final HostUsecase hostUsecase;
    private final RetrieveHostQuery retrieveHostQuery;

    @Override
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> createHost(
        @RequestBody @Valid CreateHostRequest request,
        @AuthUser PrincipalUserInfo userInfo) {
        var host = hostUsecase.createHost(request.toDomain(userInfo.getId()));

        return ResponseEntity.created(createUri("/{id}", host.getId()))
            .body(HostResource.fromDomain(host));
    }

    @Override
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> updateHost(
        @PathVariable Long id,
        @RequestBody @Valid UpdateHostRequest request,
        @AuthUser PrincipalUserInfo userInfo
    ) {
        var host = hostUsecase.updateHost(request.toDomain(id, userInfo.getId()));
        return ResponseEntity.ok(HostResource.fromDomain(host));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public void deleteHost(
        @PathVariable Long id,
        @AuthUser PrincipalUserInfo userInfo
    ) {
        hostUsecase.deleteHostById(
            HostIds.builder()
                .id(id)
                .userId(userInfo.getId())
                .build()
        );
    }

    @Override
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResources> retrieveHosts(
        @QueryParam @Valid RetrieveHostRequest retrieveAll,
        @AuthUser PrincipalUserInfo userInfo
    ) {
        RetrieveHostRequest req;
        if (retrieveAll == null) {
            req = RetrieveHostRequest.create();
        } else {
            req = retrieveAll;
        }

        var domains = retrieveHostQuery.retrieveHosts(
            req.toDomain(userInfo.getId()),
            req.getPageable()
        );

        return ResponseEntity.ok(
            new HostResources(domains));
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<HostResource> retrieveHost(
        @PathVariable Long id,
        @AuthUser PrincipalUserInfo userInfo) {
        var host = retrieveHostQuery.retrieveHostById(
            HostIds.builder()
                .id(id)
                .userId(userInfo.getId())
                .build()
        );

        return ResponseEntity.ok(HostResource.fromDomain(host));
    }
}
