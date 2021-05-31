package com.ydh.helloshop.application.controller.payment.dto.resoponse;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
public class PaymentResponsePram {

    private Long code;
    private String message;
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


    // TODO: 2021-05-31[양동혁] rest template에 objectMapper 추가
    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    private static class Response {
        private String buyerEmail;
        private String status;
        private int amount;
    }
}
