package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.reponse.VNPayResponse;
import com.project01.skillineserver.entity.PaymentEntity;
import com.project01.skillineserver.enums.PaymentMethod;
import com.project01.skillineserver.enums.PaymentStatus;
import com.project01.skillineserver.repository.PaymentRepository;
import com.project01.skillineserver.vnpay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final VNPayService vnPayService;

    @GetMapping(value = "/api/payment")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public VNPayResponse submitOrder(@RequestParam("orderId") int id,
                                     @RequestParam("amount") int orderTotal,
                                     @RequestParam("orderInfo") String orderInfo,
                                     HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal*10000, orderInfo, baseUrl,id);
        return VNPayResponse.builder()
                .URL(vnpayUrl)
                .status(200L)
                .message("Success")
                .build();
    }

    @GetMapping("/vnpay-payment/{id}")
    public ResponseEntity<Void> createPayment(@PathVariable int id, @RequestParam Map<String,String> params){

        PaymentEntity paymentEntity = PaymentEntity.builder()
                .paidAt(Instant.now())
                .paymentMethod(PaymentMethod.VNPAY)
                .status(PaymentStatus.SUCCESS)
                .amount(BigDecimal.valueOf(Double.valueOf(params.get("vnp_Amount"))/100))
                .orderId((long)id)
                .build();

        paymentRepository.save(paymentEntity);

        String targetUrl = "http://localhost:5173/success";
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(targetUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
