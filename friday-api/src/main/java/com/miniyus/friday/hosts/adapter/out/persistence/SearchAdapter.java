package com.miniyus.friday.hosts.adapter.out.persistence;

import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.hosts.adapter.out.persistence.mapper.SearchMapper;
import com.miniyus.friday.hosts.application.exception.NotFoundHostException;
import com.miniyus.friday.hosts.application.port.out.SearchPort;
import com.miniyus.friday.hosts.domain.searches.Search;
import com.miniyus.friday.hosts.domain.searches.SearchFilter;
import com.miniyus.friday.hosts.domain.searches.WhereSearch;
import com.miniyus.friday.infrastructure.persistence.repositories.HostEntityRepository;
import com.miniyus.friday.infrastructure.persistence.repositories.SearchEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class SearchAdapter implements SearchPort {
    private final HostEntityRepository hostRepository;
    private final SearchEntityRepository searchRepository;
    private final SearchMapper searchMapper;

    @Override
    public Search createSearch(Search search) {
        var hostEntity = hostRepository.findById(search.getHostId())
            .orElseThrow(NotFoundHostException::new);
        hostEntity.createSearch(searchMapper.create(search));
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
        var searchEntity = searchRepository.findById(search.getHostId())
            .orElseThrow(NotFoundHostException::new);

        searchMapper.update(searchEntity, search);

        var updated = searchRepository.save(searchEntity);

        return searchMapper.toDomain(updated);
    }

    @Override
    public Optional<Search> findSearchById(Long id) {
        return searchRepository.findById(id).map(searchMapper::toDomain);
    }

    @Override
    public Page<Search> findSearchAll(SearchFilter searchFilter) {
        return searchRepository.findSearches(searchFilter)
            .map(searchMapper::toDomain);
    }

    @Override
    public void deleteSearchById(Long id) {
        searchRepository.deleteById(id);
    }
}
