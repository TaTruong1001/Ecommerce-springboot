package com.example.doan_tatruong.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.text.ParseException;

public interface PaypalService {
    Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException;
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException, ParseException;
}
