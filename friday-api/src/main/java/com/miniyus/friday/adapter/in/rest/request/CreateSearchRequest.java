package com.miniyus.friday.adapter.in.rest.request;

import com.miniyus.friday.domain.hosts.searches.CreateSearch;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.domain.hosts.searches.SearchImage;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Builder
public record CreateSearchRequest(
    String queryKey,
    String query,
    String description,
    boolean publish,
    Long hostId
) {

    public CreateSearch toDomain(Long hostId, MultipartFile file) throws IOException {
        return CreateSearch.builder()
            .hostId(hostId)
            .queryKey(queryKey)
            .query(query)
            .description(description)
            .publish(publish)
            .searchImage(SearchImage.builder()
                .mimeType(file.getContentType())
                .size(file.getSize())
                .path(file.getOriginalFilename())
                .extension(Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1))
                .inputStream(file.getInputStream())
                .build())
            .build();
    }
}
