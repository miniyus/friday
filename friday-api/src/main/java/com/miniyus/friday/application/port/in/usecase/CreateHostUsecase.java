package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.adapter.in.rest.request.CreateHostRequest;
import com.miniyus.friday.adapter.in.rest.resource.HostResource;

public interface CreateHostUsecase {
    HostResource createHost(CreateHostRequest command, Long userId);
}
