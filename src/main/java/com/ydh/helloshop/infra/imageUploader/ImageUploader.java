package com.ydh.helloshop.infra.imageUploader;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageUploader {

    /**
     * @param multipartFile 이미지파일
     * @return 이미지 요청 url
     */
    String upload(MultipartFile multipartFile) throws IOException;
}
