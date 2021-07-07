package com.ydh.helloshop.application.controller.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.application.controller.payment.dto.PaymentParam;
import com.ydh.helloshop.application.controller.payment.dto.request.AuthenticateRequestParam;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.AuthenticateResponsePram;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.PaymentResponsePram;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import com.ydh.helloshop.application.service.DeliveryService;
import com.ydh.helloshop.infra.amqp.sender.DeliveryPublisher;
import com.ydh.helloshop.infra.config.property.IamPortProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@RestController
public class PaymentController {

    private final DeliveryService deliveryService;
    private final OrderRepository orderRepository;

    private final RestTemplate restTemplate;
    private final IamPortProperty iamPortProperty;
    private final ObjectMapper objectMapper;

    private static final String IAM_PORT_ROOT_URL = "https://api.iamport.kr";
    private static final String AUTHENTICATE_URL = "/users/getToken";
    private static final String PAYMENT_INFO_URL = "/payments/%s";

    private static final String PAID = "paid";

    public PaymentController(OrderRepository orderRepository, RestTemplateBuilder restTemplateBuilder,
                             IamPortProperty iamPortProperty, ObjectMapper objectMapper,
                             IamPortResponseErrorHandler iamPortResponseErrorHandler,
                             DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
        this.orderRepository = orderRepository;
        this.iamPortProperty = iamPortProperty;
        this.objectMapper = objectMapper;

        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(1))
                .setReadTimeout(Duration.ofSeconds(1))
                .rootUri(IAM_PORT_ROOT_URL)
                .build();
        restTemplate.setErrorHandler(iamPortResponseErrorHandler);
        this.restTemplate = restTemplate;
    }

    @Transactional
    @PostMapping("/payments/validation")
    public ResponseEntity<String> paymentValidation(@RequestBody PaymentParam paymentParam) throws JsonProcessingException {
        PaymentResponsePram paymentResponsePram = getPaymentInfo(paymentParam.getImpUid(), getAccessToken());

        Order order = orderRepository.findOne(paymentParam.getOrderId());
        if (!orderValidation(order, paymentResponsePram)) {
            return new ResponseEntity<>("잘못된 결제입니다.", HttpStatus.BAD_REQUEST);
        }
        order.setStatusPayed();

        deliveryService.publishDeliveryInfo(order.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected String getAccessToken() throws JsonProcessingException {
        HttpHeaders headers = getHttpHeaders();

        String authenticateRequestParamJson = objectMapper
                .writeValueAsString(new AuthenticateRequestParam(iamPortProperty.getImpKey(), iamPortProperty.getImpSecret()));

        HttpEntity<String> entity = new HttpEntity<>(authenticateRequestParamJson, headers);

        ResponseEntity<AuthenticateResponsePram> exchange = restTemplate
                .exchange(AUTHENTICATE_URL, HttpMethod.POST, entity, AuthenticateResponsePram.class);

        return exchange.getBody().getAccessToken();
    }

    protected PaymentResponsePram getPaymentInfo(String impUid, String accessToken) {
        HttpHeaders headers = getHttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        ResponseEntity<PaymentResponsePram> exchange = restTemplate
                .exchange(String.format(PAYMENT_INFO_URL, impUid), HttpMethod.GET, entity, PaymentResponsePram.class);
        return exchange.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private boolean orderValidation(Order order, PaymentResponsePram paymentResponsePram) {
        return memberValidation(order, paymentResponsePram) &&
                priceValidation(order, paymentResponsePram) &&
                statusValidation(paymentResponsePram.getStatus());
    }

    private boolean memberValidation(Order order, PaymentResponsePram paymentResponsePram) {
        return order.getMember().getEmail().equals(paymentResponsePram.getBuyerEmail());
    }

    private boolean priceValidation(Order order, PaymentResponsePram paymentResponsePram) {
        return order.getTotalPrice() == paymentResponsePram.getAmount();
    }

    private boolean statusValidation(String status) {
        return status.equals(PAID);
    }
}
