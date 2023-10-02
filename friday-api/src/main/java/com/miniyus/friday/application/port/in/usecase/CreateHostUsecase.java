package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.hosts.CreateHost;
import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.searches.CreateSearch;
import com.miniyus.friday.domain.hosts.searches.Search;

public interface CreateHostUsecase {
    Host createHost(CreateHost host);

    Search createSearch(CreateSearch search);
}
