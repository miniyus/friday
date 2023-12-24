package com.miniyus.friday.common.util;

import com.miniyus.friday.common.request.UploadFile;
import com.miniyus.friday.common.request.exception.ResizeImageException;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class UploadFileUtil {
    private UploadFileUtil() {
    }

    public static UploadFile of(MultipartFile multipartFile) throws IOException {
        return UploadFile.create(multipartFile);
    }

    public static JsonNullable<UploadFile> ofNullable(@Nullable MultipartFile multipartFile)
        throws IOException {
        if (multipartFile != null) {
            if (multipartFile.getBytes().length == 0) {
                return JsonNullable.of(null);
            }
            return JsonNullable.of(UploadFile.create(multipartFile));
        }
        return JsonNullable.undefined();
    }

    public static UploadFile of(File file) throws FileNotFoundException {
        MimeType mimeType;
        try {
            mimeType = getMimeType(file.getPath());
        } catch (Exception exception) {
            mimeType = MimeTypeUtils.TEXT_PLAIN;
        }

        return UploadFile.builder()
            .filename(file.getName())
            .name(file.getName())
            .size(file.length())
            .mimeType(mimeType.toString())
            .extension(UploadFileUtil.getExtension(file.getName()))
            .inputStream(new FileInputStream(file))
            .build();
    }

    public static String getExtension(MultipartFile multipartFile) {
        return getExtension(multipartFile.getOriginalFilename());
    }

    public static String getExtension(String filename) {
        return Objects.requireNonNull(filename)
            .substring(filename.lastIndexOf(".") + 1);
    }

    public static MimeType getMimeType(String filePath) throws IOException {
        Path source = Paths.get(filePath);
        String mimeType = Files.probeContentType(source);
        if (mimeType == null) {
            return MimeTypeUtils.TEXT_PLAIN;
        }
        return MimeTypeUtils.parseMimeType(mimeType);
    }

    public static MimeType getMimeType(MultipartFile multipartFile) {
        String mimeTypeString = multipartFile.getContentType();
        if (mimeTypeString == null || mimeTypeString.equals(
            MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE)) {
            return resolveTimeTypeFromExtension(getExtension(multipartFile));
        }
        return MimeTypeUtils.parseMimeType(mimeTypeString);
    }

    public static UploadFile resizeImage(
        MultipartFile multipartFile,
        int targetWidth,
        int targetHeight) throws IOException {

        try {
            var fileFormat = Objects.requireNonNull(multipartFile.getContentType());
            if (fileFormat.contains("image")) {
                BufferedImage image = ImageIO.read(multipartFile.getInputStream());
                int originWidth = image.getWidth();
                int originHeight = image.getHeight();

                if (originWidth < targetWidth && originHeight < targetHeight) {
                    return UploadFile.create(multipartFile);
                }

                MarvinImage marvinImage = new MarvinImage(image);

                Scale scale = new Scale();
                scale.load();
                scale.setAttribute("newWidth", targetWidth);
                scale.setAttribute("newHeight", targetHeight);
                scale.process(marvinImage.clone(), marvinImage, null, null, false);

                BufferedImage imageNoAlpha = marvinImage.getBufferedImageNoAlpha();
                ByteArrayOutputStream baOutStream = new ByteArrayOutputStream();
                ImageIO.write(imageNoAlpha, fileFormat, baOutStream);
                baOutStream.flush();


                var inputStream = new ByteArrayInputStream(baOutStream.toByteArray());

                return UploadFile.builder()
                    .name(multipartFile.getName())
                    .filename(multipartFile.getOriginalFilename())
                    .mimeType(multipartFile.getContentType())
                    .size((long) baOutStream.toByteArray().length)
                    .extension(getExtension(multipartFile))
                    .inputStream(inputStream)
                    .build();
            }
        } catch (IOException e) {
            throw new ResizeImageException(e.getMessage(), e);
        }

        return UploadFile.create(multipartFile);
    }

    /**
     * Resolves the MIME type based on the file extension.
     *
     * @param extension the file extension to resolve the MIME type from
     * @return the resolved MIME type
     */
    private static MimeType resolveTimeTypeFromExtension(String extension) {
        if (MimeTypeUtils.IMAGE_JPEG_VALUE.contains(extension)) {
            return MimeTypeUtils.IMAGE_JPEG;
        } else if (MimeTypeUtils.IMAGE_PNG_VALUE.contains(extension)) {
            return MimeTypeUtils.IMAGE_PNG;
        } else if (MimeTypeUtils.IMAGE_GIF_VALUE.contains(extension)) {
            return MimeTypeUtils.IMAGE_GIF;
        } else if (MimeTypeUtils.APPLICATION_JSON_VALUE.contains(extension)) {
            return MimeTypeUtils.APPLICATION_JSON;
        } else if (MimeTypeUtils.APPLICATION_XML_VALUE.contains(extension)) {
            return MimeTypeUtils.APPLICATION_XML;
        } else {
            return MimeTypeUtils.APPLICATION_OCTET_STREAM;
        }
    }
}
