package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class PaymentController {

    @Autowired
    PaymentService paymentService;


    @GetMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PaymentListResponse> getAllPayments() {

        List<PaymentEntity> allPayments = paymentService.getAllPayments();

        List<PaymentResponse> paymentResponseList = new ArrayList<>();
        for (PaymentEntity p : allPayments) {

            final PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setPaymentName(p.getPaymentName());
            paymentResponse.setId(UUID.fromString(p.getUuid()));
            paymentResponseList.add(paymentResponse);
        }


        return new ResponseEntity<>(new PaymentListResponse().paymentMethods(paymentResponseList), HttpStatus.OK);
    }

}
