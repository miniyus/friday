package com.miniyus.friday.uploads.application.port.out;

import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.uploads.domain.UploadFileDomain;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UploadPort {
    UploadFileDomain upload(UploadFile uploadFile) throws IOException;

    Optional<UploadFileDomain> findById(Long id);

    List<UploadFileDomain> findAll(Long userId);
}
