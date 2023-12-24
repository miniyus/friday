package com.miniyus.friday.hosts.application.port.in.usecase;

import com.miniyus.friday.hosts.domain.searches.CreateSearch;
import com.miniyus.friday.hosts.domain.searches.Search;
import com.miniyus.friday.hosts.domain.searches.SearchIds;
import com.miniyus.friday.hosts.domain.searches.UpdateSearch;

public interface SearchUsecase {
    Search createSearch(CreateSearch search);

    Search updateSearch(UpdateSearch search);

    void deleteSearchById(SearchIds ids);

}
