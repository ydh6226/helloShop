package com.ydh.helloshop.application.controller.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.application.controller.payment.dto.request.AuthenticateRequestParam;
import com.ydh.helloshop.application.controller.payment.dto.request.PaymentParam;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.AuthenticateResponsePram;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.PaymentResponsePram;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import com.ydh.helloshop.infra.config.property.IamPortProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final OrderRepository orderRepository;

    private final IamPortProperty iamPortProperty;
    private final ObjectMapper objectMapper;

    private static final String IAM_PORT_URL = "https://api.iamport.kr";
    private static final String AUTHENTICATE = "/users/getToken";
    private static final String PAYMENT_INFO = "/payments/%s";

    private static final String PAID = "paid";

    @PostMapping("/payments/validation")
    public ResponseEntity<String> paymentValidation(@RequestBody PaymentParam paymentParam) {
        PaymentResponsePram paymentInfo = getPaymentInfo(paymentParam.getImpUid(), getAccessToken());

        Order order = orderRepository.findOne(paymentParam.getOrderId());
        if (orderValidation(order, paymentInfo)) {
            return new ResponseEntity<>("잘못된 결제입니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean orderValidation(Order order, PaymentResponsePram paymentResponsePram) {
        if (memberValidation(order.getMember().getEmail(), paymentResponsePram.getBuyerEmail()) ||
                priceValidation(order.getTotalPrice(), paymentResponsePram.getAmount()) ||
                !statusValidation(paymentResponsePram.getStatus())) {
            return false;
        }
        return true;
    }

    private boolean priceValidation(int orderPrice, int paymentAmount) {
        return orderPrice != paymentAmount;
    }

    private boolean memberValidation(String memberEmail, String buyerEmail) {
        return memberEmail.equals(buyerEmail);
    }

    private boolean statusValidation(String status) {
        return status.equals(PAID);
    }

    public PaymentResponsePram getPaymentInfo(String impUid, String accessToken) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(headers);
        ResponseEntity<PaymentResponsePram> exchange = restTemplate.exchange(IAM_PORT_URL + String.format(PAYMENT_INFO, impUid), HttpMethod.GET, entity, PaymentResponsePram.class);
        PaymentResponsePram paymentResponsePram = exchange.getBody();
        return paymentResponsePram;

    }

    public String getAccessToken() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String authenticateRequestParamJson;
        try {
            authenticateRequestParamJson = objectMapper.
                    writeValueAsString(new AuthenticateRequestParam(iamPortProperty.getImpKey(), iamPortProperty.getImpSecret()));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("JSON 처리 에러");
        }

        HttpEntity<String> entity = new HttpEntity<>(authenticateRequestParamJson, headers);
        UriComponents url = UriComponentsBuilder.fromHttpUrl(IAM_PORT_URL + AUTHENTICATE).build();

        try {
            ResponseEntity<AuthenticateResponsePram> exchange = restTemplate
                    .exchange(url.toString(), HttpMethod.POST, entity, AuthenticateResponsePram.class);
            return exchange.getBody().getAccessToken();
        } catch (HttpStatusCodeException e) {
            log.error(e.getResponseBodyAsString());
            throw new IllegalArgumentException("IAMPORT RESTAPI 요청 에러");
        }
    }
}
