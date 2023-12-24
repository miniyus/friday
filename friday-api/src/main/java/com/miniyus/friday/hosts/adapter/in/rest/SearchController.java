package com.miniyus.friday.hosts.adapter.in.rest;

import com.miniyus.friday.common.hexagon.BaseController;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.hosts.adapter.in.rest.request.CreateSearchRequest;
import com.miniyus.friday.hosts.adapter.in.rest.resource.SearchResources;
import com.miniyus.friday.hosts.adapter.in.rest.resource.SearchResources.SearchResource;
import com.miniyus.friday.hosts.application.port.in.usecase.SearchUsecase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestAdapter(path = "/v1/hosts/{id}/searches")
@RequiredArgsConstructor
public class SearchController extends BaseController {
    private final SearchUsecase searchUsecase;

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<SearchResource> createSearch(
        @PathVariable Long id,
        @RequestPart("search") @Valid CreateSearchRequest request,
        @RequestPart("image") MultipartFile image
    ) throws IOException {
        var domain = request.toDomain(id, image);
        var returnDomain = searchUsecase.createSearch(domain);

        return ResponseEntity.created(createUri("/{id}", returnDomain.getId()))
            .body(SearchResource.fromDomain(returnDomain));
    }
}
