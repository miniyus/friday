package com.miniyus.friday.uploads.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileDomain {
    private Long id;
    private String path;
    private String originName;
    private String convName;
    private String mimeType;
    private Long size;
    private String url;
}
