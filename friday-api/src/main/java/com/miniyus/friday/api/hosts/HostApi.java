package com.miniyus.friday.api.hosts;

import com.miniyus.friday.api.hosts.request.CreateHostRequest;
import com.miniyus.friday.api.hosts.request.RetrieveHostRequest;
import com.miniyus.friday.api.hosts.request.UpdateHostRequest;
import com.miniyus.friday.api.hosts.resource.HostResources;
import com.miniyus.friday.api.hosts.resource.HostResources.HostResource;
import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.infrastructure.config.RestConfiguration;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Hosts")
public interface HostApi {
     String PATH = RestConfiguration.PREFIX + "/hosts";

     @Operation(summary = "create host")
     @PostMapping(PATH )
     ResponseEntity<HostResource> createHost(
         @RequestBody @Valid CreateHostRequest request,
         @AuthUser PrincipalUserInfo userInfo);

     @GetMapping("")
     @PreAuthorize("hasAnyAuthority('USER')")
     ResponseEntity<HostResources> retrieveHosts(
         @QueryParam @Valid RetrieveHostRequest retrieveAll,
         @PageableDefault(
             page = 1,
             sort = "createdAt", direction = Sort.Direction.DESC)
         Pageable pageable,
         @AuthUser PrincipalUserInfo userInfo
     );

     @Operation(summary = "retrieve host")
     @GetMapping(PATH + "/{id}")
     ResponseEntity<HostResource> retrieveHost(@PathVariable Long id,
         @AuthUser PrincipalUserInfo userInfo);

     @Operation(summary = "update host")
     @PatchMapping(PATH + "/{id}")
     ResponseEntity<HostResource> updateHost(@PathVariable Long id,
         @RequestBody @Valid UpdateHostRequest request,
         @AuthUser PrincipalUserInfo userInfo);

     @Operation(summary = "delete host")
     @DeleteMapping(PATH + "/{id}")
     void deleteHost(@PathVariable Long id,
         @AuthUser PrincipalUserInfo userInfo);
}
