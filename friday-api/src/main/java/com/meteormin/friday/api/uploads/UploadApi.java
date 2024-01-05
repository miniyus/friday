package com.meteormin.friday.api.uploads;

import com.meteormin.friday.api.uploads.resource.UploadFileResources;
import com.meteormin.friday.api.uploads.resource.UploadFileResources.UploadFileResource;
import com.meteormin.friday.common.request.UploadFile;
import com.meteormin.friday.infrastructure.config.RestConfiguration;
import com.meteormin.friday.infrastructure.security.PrincipalUserInfo;
import com.meteormin.friday.infrastructure.security.annotation.AuthUser;
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
