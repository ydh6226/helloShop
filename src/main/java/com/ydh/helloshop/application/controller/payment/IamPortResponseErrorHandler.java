package com.ydh.helloshop.application.controller.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.IamPortResponseParam;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.PaymentResponsePram;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class IamPortResponseErrorHandler extends DefaultResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        IamPortResponseParam iamPortResponseParam = objectMapper
                .readValue(getResponseBody(response), IamPortResponseParam.class);
        throw new IllegalArgumentException(iamPortResponseParam.getMessage());
    }
}
