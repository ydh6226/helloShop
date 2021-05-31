package com.ydh.helloshop.infra.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app.iam-port")
public class IamPortProperty {

    private String impKey;
    private String impSecret;
}
