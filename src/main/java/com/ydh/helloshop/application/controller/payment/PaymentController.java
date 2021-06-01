package com.ydh.helloshop.application.controller.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.ydh.helloshop.application.controller.payment.dto.request.AuthenticateRequestParam;
import com.ydh.helloshop.application.controller.payment.dto.PaymentParam;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.AuthenticateResponsePram;
import com.ydh.helloshop.application.controller.payment.dto.resoponse.PaymentResponsePram;
import com.ydh.helloshop.application.domain.item.Album;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import com.ydh.helloshop.infra.config.property.IamPortProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final OrderRepository orderRepository;

    private final RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;
    private final IamPortProperty iamPortProperty;
    private final ObjectMapper objectMapper;

    private static final String IAM_PORT_URL = "https://api.iamport.kr";
    private static final String AUTHENTICATE = "/users/getToken";
    private static final String PAYMENT_INFO = "/payments/%s";

    private static final String PAID = "paid";

    @PostMapping("/payments/validation")
    public ResponseEntity<String> paymentValidation(@RequestBody PaymentParam paymentParam) throws JsonProcessingException {
        PaymentResponsePram paymentResponsePram = getPaymentInfo(paymentParam.getImpUid(), getAccessToken());

        Order order = orderRepository.findOne(paymentParam.getOrderId());
        if (orderValidation(order, paymentResponsePram)) {
            return new ResponseEntity<>("잘못된 결제입니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String getAccessToken() throws JsonProcessingException {
        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders headers = getHttpHeaders();

        String authenticateRequestParamJson = objectMapper
                .writeValueAsString(new AuthenticateRequestParam(iamPortProperty.getImpKey(), iamPortProperty.getImpSecret()));

        HttpEntity<String> entity = new HttpEntity<>(authenticateRequestParamJson, headers);

        String url = UriComponentsBuilder.fromHttpUrl(IAM_PORT_URL + AUTHENTICATE)
                .build()
                .toString();

        ResponseEntity<AuthenticateResponsePram> exchange = restTemplate
                .exchange(url, HttpMethod.POST, entity, AuthenticateResponsePram.class);

        return exchange.getBody().getAccessToken();
    }

    public PaymentResponsePram getPaymentInfo(String impUid, String accessToken) {
        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders headers = getHttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(headers);

        String url = UriComponentsBuilder.fromHttpUrl(IAM_PORT_URL + String.format(PAYMENT_INFO, impUid))
                .build()
                .toString();

        ResponseEntity<PaymentResponsePram> exchange = restTemplate
                .exchange(url, HttpMethod.GET, entity, PaymentResponsePram.class);
        return exchange.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(1000);
        factory.setReadTimeout(1000);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(restTemplateResponseErrorHandler);
        return restTemplate;
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
