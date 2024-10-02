package com.example.demo.module.orders.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.controller.BaseController;
import com.example.demo.module.orders.dto.request.OrdersRequest;
import com.example.demo.module.orders.dto.response.OrdersResponse;
import com.example.demo.module.orders.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrdersController extends BaseController {
    private final OrdersService ordersService;

    @PostMapping
    public Mono<ResponseEntity<BaseResponse>> createOrder(@RequestBody OrdersRequest ordersRequest) {
        return ordersService.createOrder(ordersRequest)
                .map(this::responseCreated);
    }

    @GetMapping
    public Mono<Page<OrdersResponse>> getAllOrders(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "10") Integer limit
    ) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, limit);
        return ordersService.findAllOrders(pageable);
    }

    @GetMapping("{ordersId}")
    public Mono<ResponseEntity<BaseResponse>> getOrdersById(@PathVariable String ordersId) {
        return ordersService.findOrderById(ordersId)
                .map(this::responseSuccess);
    }

    @PatchMapping("{ordersId}")
    public Mono<ResponseEntity<BaseResponse>> updateOrder(
            @RequestBody OrdersRequest ordersRequest,
            @PathVariable String ordersId
    ) {
        return ordersService.updateOrder(ordersRequest, ordersId)
                .map(this::responseSuccess);
    }

    @DeleteMapping("{ordersId}")
    public Mono<ResponseEntity<BaseResponse>> deleteOrder(@PathVariable String ordersId) {
        return ordersService.deleteOrder(ordersId)
                .then(Mono.just(responseDeleted()));
    }
}
