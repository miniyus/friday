package com.miniyus.friday.uploads.application.port.in;

import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.uploads.domain.UploadFileDomain;

public interface UploadUsecase {
    UploadFileDomain upload(UploadFile uploadFileDomain);
}
