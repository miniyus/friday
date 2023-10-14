package com.miniyus.friday.application.port.in.query;

import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.domain.hosts.searches.SearchIds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetrieveSearchQuery {
    Page<Search> retrieveSearches(SearchFilter filter, Pageable pageable);

    Search retrieveSearch(SearchIds ids);
}
