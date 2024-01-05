package com.meteormin.friday.hosts.application.port.in.usecase;

import com.meteormin.friday.hosts.domain.searches.CreateSearch;
import com.meteormin.friday.hosts.domain.searches.PatchSearch;
import com.meteormin.friday.hosts.domain.searches.Search;
import com.meteormin.friday.hosts.domain.searches.SearchIds;

public interface SearchUsecase {
    Search createSearch(CreateSearch search);

    Search patchSearch(PatchSearch search);

    void deleteSearchById(SearchIds ids);

}
