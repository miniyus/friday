package com.miniyus.friday.hosts.adapter.out.persistence;

import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.hosts.domain.Host;
import com.miniyus.friday.hosts.domain.HostFilter;
import com.miniyus.friday.hosts.domain.WhereHost;
import com.miniyus.friday.hosts.domain.WherePublish;
import com.miniyus.friday.hosts.adapter.out.persistence.mapper.HostMapper;
import com.miniyus.friday.hosts.adapter.out.persistence.mapper.SearchMapper;
import com.miniyus.friday.hosts.application.port.out.HostPort;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.HostEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class HostAdapter extends CacheEntity<HostEntity> implements HostPort {
    private final OwnerAdapter ownerAdapter;
    private final HostEntityRepository hostRepository;
    private final HostMapper hostMapper;
    private final SearchMapper searchMapper;

    @Override
    public Host create(Host host) {
        var entity = hostMapper.create(host, ownerAdapter.getUserEntity());
        var created = hostRepository.save(entity);
        return hostMapper.toDomain(
            created,
            null
        );
    }

    @Override
    public boolean isUniqueHost(WhereHost whereHost) {
        return hostRepository.existsByHostAndUserId(whereHost.host(), whereHost.userId());
    }

    @Override
    public void deleteById(Long id) {
        hostRepository.deleteById(id);
    }

    @Override
    public Optional<Host> findByHost(WhereHost whereHost) {
        return hostRepository.findByHostAndUserId(whereHost.host(), whereHost.userId())
            .map(this::toDomain);
    }

    @Override
    public Page<Host> findByPublish(WherePublish wherePublish, Pageable pageable) {
        return hostRepository.findByPublishAndUserId(
            wherePublish.publish(),
            wherePublish.userId(),
            pageable
        ).map(this::toDomain);
    }

    @Override
    public Page<Host> findAll(Pageable pageable) {
        return hostRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Page<Host> findAll(HostFilter host, Pageable pageable) {
        return hostRepository.findHosts(
            host,
            pageable
        ).map(this::toDomain);
    }

    @Override
    public Optional<Host> findById(Long id) {
        return getCacheEntity(id)
            .map(this::toDomain);
    }

    @Override
    public Host update(Host host) {
        var entity = hostMapper.toEntity(
            host,
            ownerAdapter.getUserEntity(),
            host.getSearches()
                .stream()
                .map(searchMapper::toEntity)
                .toList()
        );

        var updated = hostRepository.save(entity);
        return this.toDomain(updated);
    }


    @Override
    protected JpaRepository<HostEntity, Long> getCacheRepository() {
        return hostRepository;
    }

    private Host toDomain(HostEntity hostEntity) {
        var searches = hostEntity.getSearches()
            .stream()
            .map(searchMapper::toDomain)
            .toList();
        return hostMapper.toDomain(hostEntity, searches);
    }

}
