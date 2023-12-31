package com.miniyus.friday.uploads.application.port.in;

import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.uploads.domain.UploadFileDomain;

import java.util.List;

public interface UploadUsecase {
    UploadFileDomain upload(UploadFile uploadFileDomain);

    UploadFileDomain findById(Long id);

    List<UploadFileDomain> findAll(Long userId);

}
