package com.miniyus.friday.api.searches;

import com.miniyus.friday.api.hosts.HostApi;
import com.miniyus.friday.api.searches.request.CreateSearchRequest;
import com.miniyus.friday.api.searches.request.RetrieveSearchRequest;
import com.miniyus.friday.api.searches.request.UpdateSearchRequest;
import com.miniyus.friday.api.searches.resource.SearchResources;
import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.miniyus.friday.api.searches.resource.SearchResources.SearchResource;

@Tag(name = "Searches")
public interface SearchApi {
    String PATH = HostApi.PATH + "/{hostId}/searches";

    @Operation(summary = "create host search")
    @PostMapping(PATH)
    ResponseEntity<SearchResource> createHostSearch(
        @PathVariable Long hostId,
        @RequestBody @Valid CreateSearchRequest request,
        @AuthUser PrincipalUserInfo userInfo);

    @Operation(summary = "retrieve host searches")
    @GetMapping(PATH)
    ResponseEntity<SearchResources> retrieveHostSearches(
        @PathVariable Long hostId,
        @QueryParam RetrieveSearchRequest request,
        @PageableDefault(page = 1,
            sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable,
        @AuthUser PrincipalUserInfo userInfo);

    @Operation(summary = "retrieve host search")
    @GetMapping(PATH + "/{id}")
    ResponseEntity<SearchResource> retrieveHostSearch(
        @PathVariable Long hostId,
        @PathVariable Long id,
        @AuthUser PrincipalUserInfo userInfo);

    @Operation(summary = "update host searches")
    @PatchMapping(PATH + "/{id}")
    ResponseEntity<SearchResource> updateHostSearch(
        @PathVariable Long hostId,
        @PathVariable Long id,
        @RequestBody @Valid UpdateSearchRequest request,
        @AuthUser PrincipalUserInfo userInfo);

    @Operation(summary = "delete host search")
    @DeleteMapping(PATH + "/{id}")
    void deleteHostSearch(
        @PathVariable Long hostId,
        @PathVariable Long id,
        @AuthUser PrincipalUserInfo userInfo);

}
