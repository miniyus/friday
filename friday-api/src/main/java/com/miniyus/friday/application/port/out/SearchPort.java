package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.domain.hosts.searches.WhereSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SearchPort {
    Optional<Search> findSearchById(Long id);

    Page<Search> findSearchAll(Long hostId, Pageable pageable);

    Page<Search> findSearchAll(SearchFilter searchFilter, Pageable pageable);

    Search createSearch(Search search);

    boolean isUniqueSearch(WhereSearch whereSearch);

    Search updateSearch(Search search);

    Optional<Search> findById(Long id);

    void deleteSearchById(Long id);
}
