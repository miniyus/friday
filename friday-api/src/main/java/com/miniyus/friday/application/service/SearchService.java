package com.miniyus.friday.application.service;

import com.miniyus.friday.application.exception.SearchNotFoundException;
import com.miniyus.friday.application.port.in.query.RetrieveSearchQuery;
import com.miniyus.friday.application.port.in.usecase.SearchUsecase;
import com.miniyus.friday.application.port.out.HostPort;
import com.miniyus.friday.application.port.out.SearchPort;
import com.miniyus.friday.common.error.ForbiddenErrorException;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.domain.hosts.HostIds;
import com.miniyus.friday.domain.hosts.searches.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Usecase
@RequiredArgsConstructor
public class SearchService implements SearchUsecase, RetrieveSearchQuery {
    private final SearchPort searchPort;
    private final HostPort hostPort;

    @Override
    public Page<Search> retrieveSearches(SearchFilter filter, Pageable pageable) {
        return searchPort.findSearchAll(filter, pageable);
    }

    @Override
    public Search retrieveSearch(SearchIds ids) {
        if (inaccessibleToSearch(ids)) {
            throw new ForbiddenErrorException();
        }

        return searchPort.findSearchById(ids.id())
            .orElseThrow(SearchNotFoundException::new);
    }

    @Override
    public Search createSearch(CreateSearch search) {
        hostPort.findById(
            search.hostId()
        ).orElseThrow(ForbiddenErrorException::new);

        return searchPort.createSearch(Search.create(search));
    }

    @Override
    public Search updateSearch(UpdateSearch updateSearch) {
        if (inaccessibleToSearch(updateSearch.ids())) {
            throw new ForbiddenErrorException();
        }

        var exists = searchPort.findSearchById(updateSearch.ids().id())
            .orElseThrow(SearchNotFoundException::new);

        exists.update(updateSearch);
        return searchPort.updateSearch(exists);
    }

    @Override
    public void deleteSearchById(SearchIds ids) {
        if (inaccessibleToSearch(ids)) {
            throw new ForbiddenErrorException();
        }

        searchPort.findSearchById(ids.id())
            .orElseThrow(SearchNotFoundException::new);
        searchPort.deleteSearchById(ids.id());
    }

    private boolean inaccessibleToSearch(SearchIds ids) {
        var host = hostPort.findById(
            ids.hostId()
        ).orElse(null);

        if (host == null) {
            return false;
        }

        if (!host.getUserId().equals(ids.userId())) {
            throw new ForbiddenErrorException();
        }

        return true;
    }
}
