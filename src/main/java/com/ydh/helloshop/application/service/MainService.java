package com.ydh.helloshop.application.service;

import com.ydh.helloshop.infra.config.property.ImageProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MainService {

    private final ImageProperty imageProperty;

    /**
     * @param file 이미지파일
     * @return 이미지 요청 url
     */
    public String imageUpload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf('.'));
        String savedFilename = UUID.randomUUID() + extension;

        file.transferTo(new File(imageProperty.getUploadPath() + "/" + savedFilename));
        return imageProperty.getDownloadUrl() + "/" + savedFilename;
    }
}
