package com.miniyus.friday.uploads.adapter.out.persistence;

import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.infrastructure.filesystem.FileSystemManager;
import com.miniyus.friday.infrastructure.filesystem.LocalFileSystemAdapter;
import com.miniyus.friday.infrastructure.persistence.repositories.FileEntityRepository;
import com.miniyus.friday.uploads.application.port.out.UploadPort;
import com.miniyus.friday.uploads.domain.UploadFileDomain;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class UploadAdapter implements UploadPort {
    private final FileSystemManager fileSystemManager;
    private final FileEntityRepository fileEntityRepository;

    @Override
    public UploadFileDomain upload(UploadFile uploadFile) throws IOException {
        var fileAdapter = fileSystemManager.getAdapterWrapper(LocalFileSystemAdapter.class);
        var fileEntity = fileAdapter.save(uploadFile.getFilename(), uploadFile);

        return UploadFileDomain.builder()
            .id(fileEntity.getId())
            .path(fileEntity.getPath())
            .originName(fileEntity.getOriginName())
            .convName(fileEntity.getConvName())
            .mimeType(fileEntity.getMimeType())
            .size(fileEntity.getSize())
            .url(fileAdapter.getUrl(fileEntity))
            .build();
    }

    @Override
    public List<UploadFileDomain> findAll() {
        return null;
    }


}
