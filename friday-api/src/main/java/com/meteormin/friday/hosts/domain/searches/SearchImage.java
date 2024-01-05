package com.meteormin.friday.hosts.domain.searches;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;

@Getter
@Builder
@AllArgsConstructor
public class SearchImage {
    private Long id;
    private String mimeType;
    private Long size;
    private String path;
    private String extension;
    private InputStream inputStream;
}
