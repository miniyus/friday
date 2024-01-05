package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.hosts.domain.HostFilter;
import com.meteormin.friday.infrastructure.persistence.entities.HostEntity;
import org.springframework.data.domain.Page;

public interface QHostEntityRepository {
    Page<HostEntity> findHosts(HostFilter hostFilter);

}
