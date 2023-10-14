package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.hosts.searches.CreateSearch;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchIds;
import com.miniyus.friday.domain.hosts.searches.UpdateSearch;

public interface SearchUsecase {
    Search createSearch(CreateSearch search);

    Search updateSearch(UpdateSearch search);

    void deleteSearchById(SearchIds ids);

}
