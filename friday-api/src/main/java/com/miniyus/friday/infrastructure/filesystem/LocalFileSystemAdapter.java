package com.miniyus.friday.infrastructure.filesystem;

import com.miniyus.friday.infrastructure.filesystem.exception.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class LocalFileSystemAdapter implements FileSystemAdapter {
    private static final String BASE_PATH = "data";

    @Override
    public boolean save(String filePath, MultipartFile multipartFile) throws IOException {
        Path path = Paths.get(makePath(BASE_PATH, filePath));

        Files.createDirectories(path.getParent());

        if (Files.exists(path)) {
            File file = new File(makePath(BASE_PATH, filePath));
            try (OutputStream outputStream = new FileOutputStream(file)) {
                int bytes = FileCopyUtils.copy(multipartFile.getInputStream(), outputStream);
                return bytes != 0;
            }
        }

        Files.createFile(path);
        return true;
    }

    @Override
    public URL getUrl(String path) {
        try {
            return new ClassPathResource(path).getURL();
        } catch (IOException e) {
            throw new FileNotFoundException("File not found", e);
        }
    }

    @Override
    public ResponseEntity<UrlResource> download(String path) {
        UrlResource resource = new UrlResource(getUrl(path));
        String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
            .body(resource);
    }

    @Override
    public boolean delete(String path) {
        try {
            return Files.deleteIfExists(Path.of(getFile(path).getPath()));
        } catch (IOException e) {
            throw new FileNotFoundException("File not found", e);
        }
    }

    @Override
    public boolean exists(String filePath) {
        Path path = Paths.get(makePath(BASE_PATH, filePath));
        return Files.exists(path);
    }

    @Override
    public File getFile(String filePath) throws IOException {
        if (exists(filePath)) {
            return new File(makePath(BASE_PATH, filePath));
        }
        return null;
    }

    /**
     * 입력한 문자열들을 파일 경로 형태로 조합
     *
     * @param base base path
     * @param path joining paths
     * @return 조합된 파일 경로
     */
    private String makePath(String base, String... path) {
        var p = Paths.get(base, path).toString();
        return p.replace("\\", "/")
            .replace("//", "/");
    }
}
