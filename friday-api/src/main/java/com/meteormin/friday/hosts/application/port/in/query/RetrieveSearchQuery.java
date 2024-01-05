package com.meteormin.friday.hosts.application.port.in.query;

import com.meteormin.friday.hosts.domain.searches.Search;
import com.meteormin.friday.hosts.domain.searches.SearchFilter;
import com.meteormin.friday.hosts.domain.searches.SearchIds;
import org.springframework.data.domain.Page;

public interface RetrieveSearchQuery {
    Page<Search> retrieveSearches(SearchFilter filter);

    Search retrieveSearch(SearchIds ids);
}
