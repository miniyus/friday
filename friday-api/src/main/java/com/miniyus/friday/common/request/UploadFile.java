package com.miniyus.friday.common.request;

import com.miniyus.friday.common.util.UploadFileUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class UploadFile implements MultipartFile {
    private String name;
    private String mimeType;
    private Long size;
    private String filename;
    private String extension;
    private InputStream inputStream;

    public static UploadFile create(@Nullable MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            return null;
        }

        MimeType mimeType = UploadFileUtil.getMimeType(multipartFile);

        var extension = UploadFileUtil.getExtension(multipartFile);

        return UploadFile.builder()
            .name(multipartFile.getName())
            .filename(multipartFile.getOriginalFilename())
            .mimeType(mimeType.toString())
            .size(multipartFile.getSize())
            .extension(extension)
            .inputStream(multipartFile.getInputStream())
            .build();
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Nullable
    @Override
    public String getOriginalFilename() {
        return filename;
    }

    @Nullable
    @Override
    public String getContentType() {
        return mimeType;
    }

    @Override
    public boolean isEmpty() {
        return inputStream == null || size == 0;
    }

    @NonNull
    @Override
    public byte[] getBytes() throws IOException {
        return inputStream.readAllBytes();
    }

    @Override
    public void transferTo(@NonNull File dest) throws IOException, IllegalStateException {
        Objects.requireNonNull(inputStream).transferTo(new FileOutputStream(dest));
    }

    /**
     * resizing for image content-type이 image가 아닐 경우, 수행 되지 않음.
     *
     * @param width  resizing width
     * @param height resizing height
     * @return resized new UploadFile instance
     */
    public UploadFile resize(int width, int height) throws IOException {
        return UploadFileUtil.resizeImage(this, width, height);
    }
}
