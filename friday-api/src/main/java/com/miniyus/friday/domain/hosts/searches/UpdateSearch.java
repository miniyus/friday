package com.miniyus.friday.domain.hosts.searches;

import org.springframework.web.multipart.MultipartFile;

public record UpdateSearch(
    SearchIds ids,
    String queryKey,
    String query,
    String description,
    boolean publish,
    SearchImage searchImage
) {
}
