package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.OrderReq;
import com.project01.skillineserver.entity.OrderEntity;
import com.project01.skillineserver.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ApiResponse<PageResponse<OrderEntity>> getOrders(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(required = false) String keyword) {

        return ApiResponse.<PageResponse<OrderEntity>>builder()
                .code(200)
                .message("Success")
                .data(orderService.getOrders(page,size,sort,keyword))
                .build();
    }

    @GetMapping(value = "/{id}")
    public ApiResponse<OrderEntity> getOrderById(@PathVariable Long id) {

        return ApiResponse.<OrderEntity>builder()
                .code(200)
                .message("Success")
                .data(orderService.getOrderById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<OrderEntity> saveOrder(@RequestBody OrderReq orderReq) {
        return ApiResponse.<OrderEntity>builder()
                .code(200)
                .data(orderService.saveOrder(orderReq))
                .message("Success")
                .build();
    }
}
