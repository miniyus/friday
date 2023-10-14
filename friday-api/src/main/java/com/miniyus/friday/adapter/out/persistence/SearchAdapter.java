package com.miniyus.friday.adapter.out.persistence;

import com.miniyus.friday.adapter.out.persistence.mapper.SearchMapper;
import com.miniyus.friday.application.exception.HostNotFoundException;
import com.miniyus.friday.application.port.out.SearchPort;
import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchFilter;
import com.miniyus.friday.domain.hosts.searches.WhereSearch;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.HostEntityRepository;
import com.miniyus.friday.infrastructure.persistence.repositories.SearchEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class SearchAdapter extends CacheEntity<SearchEntity> implements SearchPort {
    private final HostEntityRepository hostRepository;
    private final SearchEntityRepository searchRepository;
    private final SearchMapper searchMapper;

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
    public Search updateSearch(Search search) {
        var hostEntity = getCacheEntity(search.getHostId()).orElse(null);
        if (hostEntity == null) {
            return null;
        }

        var searchEntity = searchMapper.toEntity(search, hostEntity.getHost());
        var updated = searchRepository.save(searchEntity);

        return searchMapper.toDomain(updated);
    }
    @Override
    public Optional<Search> findById(Long id) {
        return Optional.empty();
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
    protected JpaRepository<SearchEntity, Long> getCacheRepository() {
        return searchRepository;
    }
}
