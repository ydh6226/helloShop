package com.ydh.helloshop.infra.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app.image")
public class ImageProperty {

    private String uploadPath;

    private String downloadUrl;
}
