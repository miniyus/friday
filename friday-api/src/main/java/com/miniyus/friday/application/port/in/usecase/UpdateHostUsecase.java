package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.UpdateHost;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.UpdateSearch;

public interface UpdateHostUsecase {
    Host updateHost(UpdateHost updateHost);

    Search updateSearch(UpdateSearch updateSearch);
}
