package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.hosts.HostIds;
import com.miniyus.friday.domain.hosts.searches.SearchIds;

public interface DeleteHostUsecase {
    void deleteById(HostIds ids);

    void deleteSearchById(SearchIds ids);
}
