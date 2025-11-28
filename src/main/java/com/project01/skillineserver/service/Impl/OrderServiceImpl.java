package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.dto.reponse.PageResponse;
import com.project01.skillineserver.dto.request.OrderReq;
import com.project01.skillineserver.dto.request.PaymentReq;
import com.project01.skillineserver.entity.CourseEntity;
import com.project01.skillineserver.entity.OrderDetailEntity;
import com.project01.skillineserver.entity.OrderEntity;
import com.project01.skillineserver.entity.UserEntity;
import com.project01.skillineserver.enums.*;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.CourseRepository;
import com.project01.skillineserver.repository.OrderDetailRepository;
import com.project01.skillineserver.repository.OrderRepository;
import com.project01.skillineserver.repository.UserRepository;
import com.project01.skillineserver.service.CourseService;
import com.project01.skillineserver.service.OrderService;
import com.project01.skillineserver.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CourseService courseService;
    private final PaymentService paymentService;

    @Override
    public PageResponse<OrderEntity> getOrders(int page, int size, String sort, String keyword) {
        Sort sortField =  Sort.by(Sort.Direction.DESC,"createdAt");
        if(sort!=null && keyword!=null){
            sortField = SortField.ASC.getValue().equalsIgnoreCase(sort) ? Sort.by(Sort.Direction.ASC,keyword) : Sort.by(Sort.Direction.DESC,keyword);
        }
        PageRequest pageRequest  = PageRequest.of(page-1, size,sortField);

        Page<OrderEntity> orders = orderRepository.findAll(pageRequest);

        return PageResponse.<OrderEntity>builder()
                .list(orders.getContent())
                .page(page)
                .size(size)
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .build();
    }

    @Override
    public OrderEntity getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.LECTURE_NOT_FOUND));
    }

    @Override
    @Transactional(rollbackFor = {AppException.class})
    public OrderEntity saveOrder(OrderReq orderReq) {

        UserEntity user = userRepository.findById(orderReq.getUserId()).orElseThrow(()->new AppException(ErrorCode.LECTURE_NOT_FOUND));

        OrderEntity orderEntity = OrderEntity.builder()
                .userId(user.getId())
                .status(OrderStatus.PAID)
                .totalPrice(orderReq.getTotalPrice())
                .createdAt(Instant.now())
                .quantity(orderReq.getQuantity())
                .build();

        courseService.purchaseCourse(orderReq.getCourseId(),user);

        OrderEntity order = orderRepository.save(orderEntity);

        List<CourseEntity> courseEntityList = courseRepository.findAllByIdIn(orderReq.getCourseId());

        List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();
        for (CourseEntity item : courseEntityList){
            orderDetailEntities.add(OrderDetailEntity.builder()
                            .orderId(order.getId())
                            .courseId(item.getId())
                            .price(item.getPrice())
                    .build());
        }
        orderDetailRepository.saveAll(orderDetailEntities);

        return order;
    }


}
