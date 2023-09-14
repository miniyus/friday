package com.miniyus.friday.hosts.adapter.in.rest;

import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.common.pagination.SimplePage;
import com.miniyus.friday.hosts.adapter.in.rest.request.CreateHostRequest;
import com.miniyus.friday.hosts.adapter.in.rest.response.CreateHostResponse;
import com.miniyus.friday.hosts.application.port.in.query.RetrieveHostQuery;
import com.miniyus.friday.hosts.application.port.in.usecase.CreateHostUsecase;
import com.miniyus.friday.hosts.application.port.in.usecase.DeleteHostUsecase;
import com.miniyus.friday.hosts.application.port.in.usecase.UpdateHostUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestAdapter(path="/v1/hosts")
@RequiredArgsConstructor
public class HostController {
    final CreateHostUsecase createHostUsecase;
    final UpdateHostUsecase updateHostUsecase;
    final DeleteHostUsecase deleteHostUsecase;
    final RetrieveHostQuery retrieveHostQuery;

    @PostMapping("")
    public ResponseEntity<CreateHostResponse> createUser(
        @RequestBody CreateHostRequest request
    ) {
        var cmd = request.toCommand();
        var host = createHostUsecase.createHost(cmd);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(host.getId())
            .toUri();

        return ResponseEntity.created(location).body(CreateHostResponse.fromDomain(host));
    }

    public ResponseEntity<?> updateUser() {
        return null;
    }

    public void deleteUser() {
    }

    public ResponseEntity<Page<?>> retrieveUsers() {
        return null;
    }

    public ResponseEntity<?> retrieveUser() {
        return null;
    }
}
