package com.miniyus.friday.api.uploads;

import com.miniyus.friday.api.uploads.resource.UploadFileResources;
import com.miniyus.friday.api.uploads.resource.UploadFileResources.UploadFileResource;
import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.infrastructure.config.RestConfiguration;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Uploads", description = "Uploads API")
public interface UploadApi {
    String PATH = RestConfiguration.PREFIX + "/uploads";

    @Operation(summary = "upload file")
    ResponseEntity<UploadFileResource> upload(UploadFile uploadFile);

    @Operation(summary = "get file")
    ResponseEntity<UploadFileResource> findById(Long id);

    @Operation(summary = "get files")
    ResponseEntity<UploadFileResources> findAll(
        @AuthUser PrincipalUserInfo userInfo);
}
