package com.miniyus.friday.hosts.adapter.out.persistence.mapper;

import com.miniyus.friday.hosts.domain.searches.Search;
import com.miniyus.friday.hosts.domain.searches.SearchImage;
import com.miniyus.friday.infrastructure.persistence.entities.FileEntity;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import org.springframework.stereotype.Component;

@Component
public class SearchMapper {
    public SearchEntity create(Search search) {
        var uploadedImage = search.getSearchImage();

        var file = FileEntity.create(
            uploadedImage.getMimeType(),
            uploadedImage.getSize(),
            uploadedImage.getPath(),
            uploadedImage.getExtension()
        );

        return SearchEntity.create(
            search.getQueryKey(),
            search.getQuery(),
            search.getDescription(),
            search.isPublish(),
            file
        );
    }

    public SearchEntity toEntity(Search domain, HostEntity host) {
        var uploadedImage = domain.getSearchImage();

        var file = FileEntity.builder()
            .id(uploadedImage.getId())
            .size(uploadedImage.getSize())
            .path(uploadedImage.getPath())
            .mimeType(uploadedImage.getMimeType())
            .extension(uploadedImage.getExtension())
            .build();

        return SearchEntity.builder()
            .id(domain.getId())
            .description(domain.getDescription())
            .queryKey(domain.getQueryKey())
            .query(domain.getQuery())
            .publish(domain.isPublish())
            .views(domain.getViews())
            .file(file)
            .host(host)
            .build();
    }

    public SearchEntity toEntity(Search domain) {
        var uploadedImage = domain.getSearchImage();

        var file = FileEntity.builder()
            .id(uploadedImage.getId())
            .size(uploadedImage.getSize())
            .path(uploadedImage.getPath())
            .mimeType(uploadedImage.getMimeType())
            .extension(uploadedImage.getExtension())
            .build();

        return SearchEntity.builder()
            .id(domain.getId())
            .description(domain.getDescription())
            .queryKey(domain.getQueryKey())
            .query(domain.getQuery())
            .publish(domain.isPublish())
            .views(domain.getViews())
            .file(file)
            .build();
    }

    public Search toDomain(SearchEntity entity) {
        var searchImage = SearchImage.builder()
            .id(entity.getFile().getId())
            .size(entity.getFile().getSize())
            .path(entity.getFile().getPath())
            .mimeType(entity.getFile().getMimeType())
            .extension(entity.getFile().getExtension())
            .build();
        return new Search(
            entity.getId(),
            entity.getQueryKey(),
            entity.getQuery(),
            entity.getDescription(),
            entity.isPublish(),
            entity.getViews(),
            entity.getShortUrl(),
            searchImage,
            entity.getDeletedAt(),
            entity.getHost().getId(),
            entity.getFile().getId()
        );
    }
}
