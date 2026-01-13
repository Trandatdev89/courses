package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.OrderReq;
import com.project01.skillineserver.entity.OrderEntity;
import com.project01.skillineserver.projection.OrderProjection;

public interface OrderService {
    PageResponse<OrderProjection> getOrders(int page, int size, String sort, String keyword);
    OrderEntity getOrderById(Long id);
    OrderEntity saveOrder(OrderReq orderReq);
}
