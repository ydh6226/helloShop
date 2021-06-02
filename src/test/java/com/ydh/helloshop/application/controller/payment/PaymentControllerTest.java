package com.ydh.helloshop.application.controller.payment;

import com.ydh.helloshop.application.controller.payment.dto.resoponse.PaymentResponsePram;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentControllerTest {

    @Autowired
    PaymentController paymentController;

    private static final String impUid = "imp_367450343294";

    @Test
    @DisplayName("iamport api 요청")
    void requestApi() throws Exception {
        String accessToken = paymentController.getAccessToken();
        assertThat(accessToken).isNotNull();

        PaymentResponsePram paymentInfo = paymentController.getPaymentInfo(impUid, accessToken);
        assertThat(paymentInfo).isNotNull();
    }

}