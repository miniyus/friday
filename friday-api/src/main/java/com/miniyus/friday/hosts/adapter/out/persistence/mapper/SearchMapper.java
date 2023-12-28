package com.miniyus.friday.hosts.adapter.out.persistence.mapper;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.hosts.domain.searches.Search;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.FileEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchMapper {
    private final FileEntityRepository fileEntityRepository;

    public SearchEntity create(Search search) {
        var fileEntity = fileEntityRepository.findById(search.getFileId())
            .orElseThrow(
                () -> new RestErrorException(RestErrorCode.NOT_FOUND, "file.error.notFound"));

        return SearchEntity.create(
            search.getQueryKey(),
            search.getQuery(),
            search.getDescription(),
            search.isPublish(),
            fileEntity
        );
    }

    public SearchEntity update(SearchEntity entity, Search domain) {
        return entity.setId(domain.getId())
            .setDescription(domain.getDescription())
            .setQueryKey(domain.getQueryKey())
            .setQuery(domain.getQuery())
            .setPublish(domain.isPublish())
            .setViews(domain.getViews());
    }

    public Search toDomain(SearchEntity entity) {
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
            .fileId(entity.getFile().getId())
            .build();
    }
}
