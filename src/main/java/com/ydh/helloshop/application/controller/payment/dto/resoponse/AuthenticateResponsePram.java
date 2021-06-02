package com.ydh.helloshop.application.controller.payment.dto.resoponse;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthenticateResponsePram extends IamPortResponseParam {

    private Response response;

    public String getAccessToken() {
        return response.getAccessToken();
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class Response {
        private String accessToken;
        private Long expiredAt;
        private Long now;
    }
}
