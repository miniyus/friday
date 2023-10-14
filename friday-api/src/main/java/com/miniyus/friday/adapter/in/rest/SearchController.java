package com.miniyus.friday.adapter.in.rest;

import com.miniyus.friday.adapter.in.rest.request.CreateSearchRequest;
import com.miniyus.friday.application.port.in.usecase.SearchUsecase;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.miniyus.friday.adapter.in.rest.resource.SearchResources.SearchResource;

@RestAdapter(path = "/v1/hosts/{id}/searches")
@RequiredArgsConstructor
public class SearchController {
    private final SearchUsecase searchUsecase;
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<SearchResource> createSearch(
        @PathVariable Long id,
        @RequestPart("search") @Valid CreateSearchRequest request,
        @RequestPart("image") MultipartFile image
    ) throws IOException {
        var domain = request.toDomain(id, image);
        searchUsecase.createSearch(domain);
    }
}
