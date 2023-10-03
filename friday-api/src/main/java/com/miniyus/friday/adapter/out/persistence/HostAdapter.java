package com.miniyus.friday.adapter.out.persistence;

import com.miniyus.friday.adapter.out.persistence.mapper.HostMapper;
import com.miniyus.friday.adapter.out.persistence.mapper.SearchMapper;
import com.miniyus.friday.application.exception.HostNotFoundException;
import com.miniyus.friday.application.port.out.CreateHostPort;
import com.miniyus.friday.application.port.out.DeleteHostPort;
import com.miniyus.friday.application.port.out.RetrieveHostPort;
import com.miniyus.friday.application.port.out.UpdateHostPort;
import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.HostFilter;
import com.miniyus.friday.domain.hosts.WhereHost;
import com.miniyus.friday.domain.hosts.WherePublish;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.domain.hosts.searches.WhereSearch;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.HostEntityRepository;
import com.miniyus.friday.infrastructure.persistence.repositories.SearchEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class HostAdapter extends CacheEntity<HostEntity>
    implements CreateHostPort, UpdateHostPort, DeleteHostPort,
    RetrieveHostPort {
    private final OwnerAdapter ownerAdapter;
    private final HostEntityRepository hostRepository;
    private final SearchEntityRepository searchRepository;
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
    public Search createSearch(Search search) {
        var hostEntity = hostRepository.findById(search.getHostId())
            .orElseThrow(HostNotFoundException::new);

        hostEntity.createSearch(
            searchMapper.create(search)
        );

        hostRepository.save(hostEntity);

        return searchRepository.findLastSearchByHostId(hostEntity.getId())
            .map(searchMapper::toDomain)
            .orElse(null);
    }

    @Override
    public boolean isUniqueSearch(WhereSearch whereSearch) {
        return searchRepository.existsByHostIdAndQueryKeyAndQuery(
            whereSearch.hostId(),
            whereSearch.queryKey(),
            whereSearch.query()
        );
    }

    @Override
    public void deleteById(Long id) {
        hostRepository.deleteById(id);
    }

    @Override
    public Optional<Search> findSearchById(Long id) {
        return searchRepository.findById(id).map(searchMapper::toDomain);
    }

    @Override
    public Page<Search> findSearchAll(Long hostId, Pageable pageable) {
        return searchRepository.findAllByHostId(hostId, pageable)
            .map(searchMapper::toDomain);
    }

    @Override
    public Page<Search> findSearchAll(SearchFilter searchFilter, Pageable pageable) {
        return searchRepository.findSearches(searchFilter, pageable)
            .map(searchMapper::toDomain);
    }

    @Override
    public void deleteSearchById(Long id) {
        searchRepository.deleteById(id);
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
    public Search updateSearch(Search search) {
        var hostEntity = getCacheEntity(search.getHostId()).orElse(null);
        if (hostEntity == null) {
            return null;
        }

        var searchEntity = searchMapper.toEntity(search, hostEntity);
        var updated = searchRepository.save(searchEntity);

        return searchMapper.toDomain(updated);
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
