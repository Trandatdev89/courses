package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.request.PaymentReq;

public interface PaymentService {
    void createPayment(PaymentReq paymentReq);
}
