package com.ydh.helloshop.application.controller.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import com.ydh.helloshop.infra.config.property.IamPortProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(value = PaymentController.class)
class PaymentControllerRestTest {

    @Autowired
    PaymentController paymentController;

    @Autowired
    MockRestServiceServer mockServer;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    IamPortResponseErrorHandler iamPortResponseErrorHandler;

    @MockBean
    IamPortProperty iamPortProperty;

    @Test
    void test() throws Exception {
        String ret = "{\n" +
                "    \"code\": 0,\n" +
                "    \"message\": null,\n" +
                "    \"response\": {\n" +
                "        \"access_token\": \"4ba29f5f2937e184523cfbca1ee40edfae76a975\",\n" +
                "        \"now\": 1622629267,\n" +
                "        \"expired_at\": 1622631067\n" +
                "    }\n" +
                "}";

        mockServer.expect(anything())
                .andRespond(withSuccess(ret, MediaType.APPLICATION_JSON));

        when(iamPortProperty.getImpKey()).thenReturn("1");
        when(iamPortProperty.getImpSecret()).thenReturn("1");

        String accessToken = paymentController.getAccessToken();
        assertThat(accessToken).isEqualTo("4ba29f5f2937e184523cfbca1ee40edfae76a975");
    }
}