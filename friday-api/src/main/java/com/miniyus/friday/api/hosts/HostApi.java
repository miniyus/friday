package com.miniyus.friday.api.hosts;

import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.hosts.adapter.in.rest.request.CreateHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.request.RetrieveHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.request.UpdateHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.resource.HostResources;
import com.miniyus.friday.hosts.adapter.in.rest.resource.HostResources.HostResource;
import com.miniyus.friday.infrastructure.config.RestConfiguration;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface HostApi {
     String PATH = RestConfiguration.PREFIX + "/hosts";

     @Operation(summary = "create host")
     @PostMapping(PATH )
     ResponseEntity<HostResource> createHost(
         @RequestBody @Valid CreateHostRequest request,
         @AuthUser PrincipalUserInfo userInfo);

     @Operation(summary = "retrieve host")
     @GetMapping(PATH + "/{id}")
     ResponseEntity<HostResource> retrieveHost(@PathVariable Long id,
         @AuthUser PrincipalUserInfo userInfo);

     @Operation(summary = "retrieve hosts")
     @GetMapping(PATH)
     ResponseEntity<HostResources> retrieveHosts(
         @QueryParam @Valid RetrieveHostRequest retrieveAll,
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
