package com.miniyus.friday.infrastructure.filesystem;

import com.miniyus.friday.infrastructure.filesystem.exception.FileNotFoundException;
import com.miniyus.friday.infrastructure.persistence.entities.FileEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.FileEntityRepository;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileSystemManager {
    private static final String BASE_PATH = "data";
    private final List<FileSystemAdapter> adapters;
    private final FileEntityRepository fileEntityRepository;
    private final Map<String, FileSystemAdapterWrapper> fileSystemAdapterWrappers = new HashMap<>();

    private FileSystemAdapter getAdapter(Class<? extends FileSystemAdapter> clazz) {
        return adapters.stream()
            .filter(a -> clazz.isAssignableFrom(a.getClass()))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("not found adapter"));
    }

    public FileSystemAdapterWrapper getAdapterWrapper(Class<? extends FileSystemAdapter> clazz) {
        if (fileSystemAdapterWrappers.containsKey(clazz.getName())) {
            return fileSystemAdapterWrappers.get(clazz.getName());
        } else {
            var wrapper = new FileSystemAdapterWrapper(getAdapter(clazz), fileEntityRepository);
            fileSystemAdapterWrappers.put(clazz.getName(), wrapper);
            return wrapper;
        }
    }

    @RequiredArgsConstructor
    public static class FileSystemAdapterWrapper {
        private final FileSystemAdapter adapter;
        private final FileEntityRepository fileEntityRepository;

        public FileEntity save(String path, MultipartFile multipartFile) throws IOException {
            if (multipartFile == null || multipartFile.isEmpty()) {
                throw new FileNotFoundException("File is empty", null);
            }

            var uuid = UUID.randomUUID();
            var clientPath = Objects.requireNonNull(getUserInfo().getId()).toString();

            String originName;
            if (multipartFile.getOriginalFilename() == null) {
                originName = uuid.toString();
            } else {
                originName = multipartFile.getOriginalFilename();
            }

            String mimeType;
            if (multipartFile.getContentType() == null) {
                mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            } else {
                mimeType = multipartFile.getContentType();
            }

            var fileEntity = FileEntity.builder()
                .path(makePath(clientPath, path, uuid.toString()))
                .originName(originName)
                .size(multipartFile.getSize())
                .convName(uuid.toString())
                .mimeType(mimeType)
                .build();
            var save = fileEntityRepository.save(fileEntity);
            adapter.save(fileEntity.getPath(), multipartFile);

            return save;
        }

        public Optional<FileEntity> findById(Long id) {
            return fileEntityRepository.findById(id);
        }

        public String getUrl(Long id) {
            var entity = findById(id);
            if (entity.isEmpty()) {
                return null;
            } else {
                var url = entity.get().getPath();
                return adapter.getUrl(url).toString();
            }
        }

        public String getUrl(FileEntity entity) {
            return adapter.getUrl(entity.getPath())
                .toString();
        }

        public File getFile(FileEntity entity) throws IOException {
            return adapter.getFile(entity.getPath());
        }

        private String makePath(String... path) {
            var p = Paths.get(FileSystemManager.BASE_PATH, path).toString();
            return p.replace("\\", "/")
                .replace("//", "/");
        }

        private PrincipalUserInfo getUserInfo() {
            return (PrincipalUserInfo) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        }
    }
}
