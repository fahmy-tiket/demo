package com.example.demo.module.orders.service;

import com.example.demo.module.orders.dto.request.OrdersRequest;
import com.example.demo.module.orders.dto.response.OrdersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface OrdersService {
    Mono<Object> createOrder(OrdersRequest ordersRequest);
    Mono<Page<OrdersResponse>> findAllOrders(Pageable pageable);
    Mono<OrdersResponse> findOrderById(String invoiceNumber);
    Mono<OrdersResponse> updateOrder(OrdersRequest ordersRequest, String ordersId);
    Mono<Void> deleteOrder(String ordersId);
}
