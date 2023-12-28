package com.miniyus.friday.hosts.application.port.out;

import com.miniyus.friday.hosts.domain.searches.Search;
import com.miniyus.friday.hosts.domain.searches.SearchFilter;
import com.miniyus.friday.hosts.domain.searches.WhereSearch;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface SearchPort {
    Optional<Search> findSearchById(Long id);

    Page<Search> findSearchAll(SearchFilter searchFilter);

    Search createSearch(Search search);

    boolean isUniqueSearch(WhereSearch whereSearch);

    Search updateSearch(Search search);

    void deleteSearchById(Long id);
}
