package com.ydh.helloshop.infra.imageUploader;

import com.ydh.helloshop.infra.config.property.ImageProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Profile("!prod")
@Component
@RequiredArgsConstructor
public class LocalImageUploader implements ImageUploader {

    private final ImageProperty imageProperty;

    @Override
    public String upload(MultipartFile multipartFile) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File(imageProperty.getUploadPath() + "/" + filename));
        return imageProperty.getDownloadUrl() + "/" + filename;
    }
}
