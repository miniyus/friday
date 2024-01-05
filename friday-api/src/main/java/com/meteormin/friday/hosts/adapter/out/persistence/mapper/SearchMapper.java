package com.meteormin.friday.hosts.adapter.out.persistence.mapper;

import com.meteormin.friday.hosts.application.exception.NotFoundHostException;
import com.meteormin.friday.hosts.domain.searches.Search;
import com.meteormin.friday.infrastructure.persistence.entities.FileEntity;
import com.meteormin.friday.infrastructure.persistence.entities.SearchEntity;
import com.meteormin.friday.infrastructure.persistence.entities.SearchFileEntity;
import com.meteormin.friday.infrastructure.persistence.repositories.FileEntityRepository;
import com.meteormin.friday.infrastructure.persistence.repositories.HostEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchMapper {
    private final HostEntityRepository hostRepository;
    private final FileEntityRepository fileEntityRepository;

    public SearchEntity createSearchEntity(Search search) {
        var hostEntity = hostRepository.findById(search.getHostId())
            .orElseThrow(NotFoundHostException::new);
        var fileEntities = fileEntityRepository.findByIdIn(search.getFiles());
        var entity = SearchEntity.create(
            search.getQueryKey(),
            search.getQuery(),
            search.getDescription(),
            search.isPublish()
        );

        fileEntities.forEach(fileEntity -> entity.getSearchFiles()
            .add(SearchFileEntity.builder()
                .file(fileEntity)
                .build()));
        return entity.setHost(hostEntity);
    }

    public SearchEntity updateSearchEntity(SearchEntity entity, Search domain) {
        return entity.setId(domain.getId())
            .setDescription(domain.getDescription())
            .setQueryKey(domain.getQueryKey())
            .setQuery(domain.getQuery())
            .setPublish(domain.isPublish())
            .setViews(domain.getViews());
    }

    public Search toSearchDomain(SearchEntity entity) {
        return Search.builder()
            .id(entity.getId())
            .queryKey(entity.getQueryKey())
            .query(entity.getQuery())
            .description(entity.getDescription())
            .publish(entity.isPublish())
            .views(entity.getViews())
            .shortUrl(entity.getShortUrl())
            .deletedAt(entity.getDeletedAt())
            .hostId(entity.getHost().getId())
            .files(entity.getSearchFiles().stream()
                .map(SearchFileEntity::getFile)
                .map(FileEntity::getId).toList())
            .build();
    }
}
