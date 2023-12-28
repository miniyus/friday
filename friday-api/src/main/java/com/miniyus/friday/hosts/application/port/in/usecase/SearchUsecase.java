package com.miniyus.friday.hosts.application.port.in.usecase;

import com.miniyus.friday.hosts.domain.searches.CreateSearch;
import com.miniyus.friday.hosts.domain.searches.PatchSearch;
import com.miniyus.friday.hosts.domain.searches.Search;
import com.miniyus.friday.hosts.domain.searches.SearchIds;

public interface SearchUsecase {
    Search createSearch(CreateSearch search);

    Search patchSearch(PatchSearch search);

    void deleteSearchById(SearchIds ids);

}
