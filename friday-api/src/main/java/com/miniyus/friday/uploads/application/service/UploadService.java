package com.miniyus.friday.uploads.application.service;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.uploads.application.exception.NotFoundUploadFileException;
import com.miniyus.friday.uploads.application.port.in.UploadUsecase;
import com.miniyus.friday.uploads.application.port.out.UploadPort;
import com.miniyus.friday.uploads.domain.UploadFileDomain;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@Usecase
@RequiredArgsConstructor
public class UploadService implements UploadUsecase {
    private final UploadPort port;

    @Override
    public UploadFileDomain upload(UploadFile uploadFile) {
        try {
            return port.upload(uploadFile);
        } catch (IOException e) {
            throw new RestErrorException(RestErrorCode.UPLOAD_ERROR, e);
        }
    }

    @Override
    public UploadFileDomain findById(Long id) {
        return port.findById(id)
            .orElseThrow(NotFoundUploadFileException::new);
    }

    @Override
    public List<UploadFileDomain> findAll(Long userId) {
        return port.findAll(userId);
    }
}
