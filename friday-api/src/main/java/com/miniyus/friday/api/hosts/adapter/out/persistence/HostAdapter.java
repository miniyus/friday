package com.miniyus.friday.api.hosts.adapter.out.persistence;

import com.miniyus.friday.api.hosts.application.port.out.CreateHostPort;
import com.miniyus.friday.api.hosts.application.port.out.DeleteHostPort;
import com.miniyus.friday.api.hosts.application.port.out.RetrieveHostPort;
import com.miniyus.friday.api.hosts.application.port.out.UpdateHostPort;
import com.miniyus.friday.api.hosts.domain.Host;
import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.infrastructure.persistence.repositories.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@RequiredArgsConstructor
@PersistenceAdapter
public class HostAdapter implements CreateHostPort, UpdateHostPort, DeleteHostPort,
    RetrieveHostPort {
    private final HostRepository hostRepository;
    private final HostMapper mapper;

    @Override
    public Host create(Host host) {
        var entity = mapper.toEntity(host);
        var created = hostRepository.save(entity);
        return mapper.toDomain(created);
    }

    @Override
    public boolean isUniqueHost(Host.WhereHost whereHost) {
        return hostRepository.existsByHostAndUserId(whereHost.host(), whereHost.userId());
    }

    @Override
    public void deleteById(Long id) {
        hostRepository.deleteById(id);
    }

    @Override
    public Optional<Host> findByHost(Host.WhereHost whereHost) {
        return hostRepository.findByHostAndUserId(whereHost.host(), whereHost.userId())
            .map(mapper::toDomain);
    }

    @Override
    public Page<Host> findByPublish(Host.WherePublish wherePublish, Pageable pageable) {
        return hostRepository.findByPublishAndUserId(
            wherePublish.publish(),
            wherePublish.userId(),
            pageable
        ).map(mapper::toDomain);
    }

    @Override
    public Page<Host> findAll(Pageable pageable) {
        return hostRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Host> findAll(Host.HostFilter host, Pageable pageable) {
        return hostRepository.findAll(
            host.summary(),
            host.description(),
            host.path(),
            host.userId(),
            host.createdAtStart(),
            host.createdAtEnd(),
            host.updatedAtStart(),
            host.updatedAtEnd(),
            pageable
        ).map(mapper::toDomain);
    }

    @Override
    public Optional<Host> findById(Long id) {
        return hostRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Host update(Host host) {
        var entity = mapper.toEntity(host);
        var updated = hostRepository.save(entity);
        return mapper.toDomain(updated);
    }
}
