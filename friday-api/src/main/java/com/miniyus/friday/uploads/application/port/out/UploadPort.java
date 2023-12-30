package com.miniyus.friday.uploads.application.port.out;

import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.uploads.domain.UploadFileDomain;

import java.io.IOException;
import java.util.List;

public interface UploadPort {
    UploadFileDomain upload(UploadFile uploadFile) throws IOException;

    List<UploadFileDomain> findAll();
}
