package com.miniyus.friday.uploads.application.service;

import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.uploads.application.port.in.UploadUsecase;
import com.miniyus.friday.uploads.application.port.out.UploadPort;
import com.miniyus.friday.uploads.domain.UploadFileDomain;
import lombok.RequiredArgsConstructor;

@Usecase
@RequiredArgsConstructor
public class UploadService implements UploadUsecase {
    private final UploadPort port;
    @Override
    public UploadFileDomain upload(UploadFile uploadFile) {
        return null;
    }
}
