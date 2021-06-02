package com.ydh.helloshop.application.controller.payment.dto.resoponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentResponsePram extends IamPortResponseParam {

    public Response response;

    public String getBuyerEmail() {
        return response.getBuyerEmail();
    }

    public String getStatus() {
        return response.getStatus();
    }

    public int getAmount() {
        return response.getAmount();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class Response {
        private String buyerEmail;
        private String status;
        private int amount;
    }
}
