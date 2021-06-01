package com.ydh.helloshop.application.controller.payment;

import com.ydh.helloshop.application.controller.payment.dto.resoponse.PaymentResponsePram;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentControllerTest {

    @Autowired
    PaymentController paymentController;

    @Test
    void test() throws Exception {
        String accessToken = paymentController.getAccessToken();
        System.out.println("accessToken = " + accessToken);
        PaymentResponsePram imp_031600503933 = paymentController.getPaymentInfo("imp_031600503933", accessToken);
        System.out.println(imp_031600503933.getAmount());
    }
}