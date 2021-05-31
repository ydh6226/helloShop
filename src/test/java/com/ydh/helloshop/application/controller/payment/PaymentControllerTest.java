package com.ydh.helloshop.application.controller.payment;

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
        paymentController.getPaymentInfo("imp_031600503933", accessToken);
    }




}