package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.request.PaymentReq;
import com.project01.skillineserver.entity.PaymentEntity;
import com.project01.skillineserver.repository.PaymentRepository;
import com.project01.skillineserver.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public void createPayment(PaymentReq paymentReq) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paymentMethod(paymentReq.paymentMethod())
                .status(paymentReq.paymentStatus())
                .orderId(paymentReq.orderId())
                .paidAt(Instant.now())
                .build();

        paymentRepository.save(paymentEntity);

    }
}
