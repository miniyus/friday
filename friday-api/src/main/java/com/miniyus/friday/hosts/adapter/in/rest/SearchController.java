package com.miniyus.friday.hosts.adapter.in.rest;

import com.miniyus.friday.api.hosts.SearchApi;
import com.miniyus.friday.common.hexagon.BaseController;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.hosts.adapter.in.rest.request.CreateSearchRequest;
import com.miniyus.friday.hosts.adapter.in.rest.request.UpdateSearchRequest;
import com.miniyus.friday.hosts.adapter.in.rest.resource.SearchResources;
import com.miniyus.friday.hosts.adapter.in.rest.resource.SearchResources.SearchResource;
import com.miniyus.friday.hosts.application.port.in.usecase.SearchUsecase;
import com.miniyus.friday.hosts.domain.searches.CreateSearch;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.annotation.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestAdapter(path = SearchApi.PATH)
@RequiredArgsConstructor
public class SearchController extends BaseController implements SearchApi {
    private final SearchUsecase searchUsecase;

    @Override
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<SearchResource> createHostSearch(
        @PathVariable Long hostId,
        @RequestBody @Valid CreateSearchRequest request,
        @AuthUser PrincipalUserInfo userInfo) {
        var domain = searchUsecase.createSearch(
            request.toDomain(hostId));

        return ResponseEntity.created(createUri("/{searchId}", domain.getId()))
            .body(SearchResource.fromDomain(domain));
    }

    @Override
    public ResponseEntity<SearchResources> retrieveHostSearches(Long hostId,
        PrincipalUserInfo userInfo) {
        return null;
    }

    @Override
    public ResponseEntity<SearchResource> retrieveHostSearch(Long hostId, Long id,
        PrincipalUserInfo userInfo) {
        return null;
    }

    @Override
    public ResponseEntity<SearchResource> updateHostSearch(Long hostId, Long id,
        UpdateSearchRequest request, PrincipalUserInfo userInfo) {
        return null;
    }

    @Override
    public void deleteHostSearch(Long hostId, Long id, PrincipalUserInfo userInfo) {

    }
}
