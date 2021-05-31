package com.ydh.helloshop.application.controller.payment.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthenticateRequestParam {

    private String impKey;
    private String impSecret;

    public AuthenticateRequestParam(String impKey, String impSecret) {
        this.impKey = impKey;
        this.impSecret = impSecret;
    }
}
