package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.WhereHost;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.WhereSearch;

public interface CreateHostPort {
    Host create(Host host);

    boolean isUniqueHost(WhereHost whereHost);

    Search createSearch(Search search);

    boolean isUniqueSearch(WhereSearch whereSearch);
}
