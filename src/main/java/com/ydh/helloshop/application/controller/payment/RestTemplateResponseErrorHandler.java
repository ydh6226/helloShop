package com.ydh.helloshop.application.controller.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.IamPortResponseParam;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.PaymentResponsePram;
import com.ydh.helloshop.application.domain.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        IamPortResponseParam iamPortResponseParam = objectMapper
                .readValue(getResponseBody(response), PaymentResponsePram.class);
        throw new IllegalArgumentException(iamPortResponseParam.getMessage());
    }
}
