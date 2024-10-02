package com.example.demo.module.orders.service.impl;

import com.example.demo.entity.Customers;
import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.Orders;
import com.example.demo.exception.OrdersAlreadyExistException;
import com.example.demo.exception.OrdersNotFoundException;
import com.example.demo.module.orders.dto.request.OrdersRequest;
import com.example.demo.module.orders.dto.response.OrdersResponse;
import com.example.demo.module.orders.service.OrdersService;
import com.example.demo.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    @Override
    public Mono<Object> createOrder(OrdersRequest ordersRequest) {
        return ordersRepository.findByInvoiceNumberEqualsIgnoreCase(ordersRequest.getInvoiceNumber())
                .flatMap(existingOrder -> Mono.error(new OrdersAlreadyExistException("Order already exists for Invoice: ".concat(ordersRequest.getInvoiceNumber())))) // If exists, throw an error
                .switchIfEmpty(Mono.defer(() -> {
                    Orders newOrder = new Orders();
                    newOrder.setInvoiceNumber(ordersRequest.getInvoiceNumber());
                    newOrder.setAmount(ordersRequest.getAmount());
                    newOrder.setCustomer(ordersRequest.getCustomer());
                    newOrder.setOrderDetails(ordersRequest.getOrderDetails());
                    newOrder.setAdditionalInfo(ordersRequest.getAdditionalInfo());

                    return ordersRepository.save(newOrder)
                            .map(this::convertToResponse);
                }));
    }

    @Override
    public Mono<Page<OrdersResponse>> findAllOrders(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        Flux<Orders> ordersFlux = ordersRepository.findAll()
                .skip((long) page * size)
                .take(size);

        Mono<Long> totalCountMono = ordersRepository.count();
        return ordersFlux
                .map(this::convertToResponse)
                .collectList()
                .zipWith(totalCountMono)
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }

    @Override
    public Mono<OrdersResponse> findOrderById(String ordersId) {
        return ordersRepository.findById(ordersId)
                .switchIfEmpty(Mono.error(new OrdersNotFoundException("Orders not found for Invoice:".concat(ordersId))))
                .map(this::convertToResponse);
    }

    @Override
    public Mono<OrdersResponse> updateOrder(OrdersRequest ordersRequest, String ordersId) {
        return ordersRepository.findById(ordersId)
                .switchIfEmpty(Mono.error(new OrdersNotFoundException("Orders not found for ID:".concat(ordersId))))
                .flatMap(updatedOrder -> {
                    updatedOrder.setAmount(ordersRequest.getAmount());
                    updatedOrder.setOrderDetails(ordersRequest.getOrderDetails());
                    updatedOrder.setAdditionalInfo(ordersRequest.getAdditionalInfo());
                    return ordersRepository.save(updatedOrder);
                })
                .flatMap(savedOrder -> {
                    OrdersResponse response = convertToResponse(savedOrder);
                    return response != null ? Mono.just(response) : Mono.error(new NullPointerException("Conversion to OrdersResponse returned null"));
                });
    }

    @Override
    public Mono<Void> deleteOrder(String ordersId) {
        return ordersRepository.findById(ordersId)
                .switchIfEmpty(Mono.error(new OrdersNotFoundException("Orders not found for ID:".concat(ordersId))))
                .flatMap(order -> ordersRepository.delete(order).then());
    }

    public OrdersResponse convertToResponse(Orders orders) {
        return OrdersResponse.builder()
                .id(orders.getId())
                .invoiceNumber(orders.getInvoiceNumber())
                .amount(orders.getAmount())
                .customer(Customers.builder()
                        .name(orders.getCustomer().getName())
                        .email(orders.getCustomer().getEmail())
                        .phone(orders.getCustomer().getPhone())
                        .address(orders.getCustomer().getAddress())
                        .build())
                .orderDetails(orders.getOrderDetails().stream()
                        .map(this::mapOrderDetails).toList())
                .additionalInfo(Optional.ofNullable(orders.getAdditionalInfo())
                        .orElse(Collections.emptyMap()))
                .build();
    }

    public OrderDetails mapOrderDetails(OrderDetails orderDetails) {
        return OrderDetails.builder()
                .item(orderDetails.getItem())
                .quantity(orderDetails.getQuantity())
                .amount(orderDetails.getAmount())
                .build();
    }
}
