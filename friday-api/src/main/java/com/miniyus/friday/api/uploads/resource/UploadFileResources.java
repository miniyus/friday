package com.miniyus.friday.api.uploads.resource;

import com.miniyus.friday.uploads.domain.UploadFileDomain;
import lombok.Builder;

import java.util.List;

public record UploadFileResources(
    List<UploadFileResource> files
) {
    public static UploadFileResources fromDomains(List<UploadFileDomain> domains) {
        return new UploadFileResources(
            domains.stream()
                .map(UploadFileResource::fromDomain)
                .toList());
    }

    @Builder
    public record UploadFileResource(
        Long id,
        String originName,
        String convName,
        String mimeType,
        Long size,
        String url
    ) {
        public static UploadFileResource fromDomain(UploadFileDomain domain) {
            return builder()
                .id(domain.getId())
                .originName(domain.getOriginName())
                .convName(domain.getConvName())
                .mimeType(domain.getMimeType())
                .size(domain.getSize())
                .url(domain.getUrl())
                .build();
        }
    }
}
