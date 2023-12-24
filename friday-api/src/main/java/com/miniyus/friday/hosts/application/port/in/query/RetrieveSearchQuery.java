package com.miniyus.friday.hosts.application.port.in.query;

import com.miniyus.friday.hosts.domain.searches.Search;
import com.miniyus.friday.hosts.domain.searches.SearchFilter;
import com.miniyus.friday.hosts.domain.searches.SearchIds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RetrieveSearchQuery {
    Page<Search> retrieveSearches(SearchFilter filter, Pageable pageable);

    Search retrieveSearch(SearchIds ids);
}
