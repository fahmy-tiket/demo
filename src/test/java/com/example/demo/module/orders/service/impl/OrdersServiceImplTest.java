package com.example.demo.module.orders.service.impl;

import com.example.demo.entity.Customers;
import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.Orders;
import com.example.demo.module.orders.dto.request.OrdersRequest;
import com.example.demo.module.orders.dto.response.OrdersResponse;
import com.example.demo.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
class OrdersServiceImplTest {
    @InjectMocks
    private OrdersServiceImpl ordersService;

    @Mock
    private OrdersRepository ordersRepository;

    public static OrdersRequest ordersRequest = new OrdersRequest();
    public static OrdersResponse ordersResponse = new OrdersResponse();
    public static Orders orders = new Orders();
    public static OrderDetails orderDetails = new OrderDetails();
    public static Customers customers = new Customers();

    @BeforeEach
    void setUp() {
        orderDetails.setItem("Baju");
        orderDetails.setQuantity(2);
        orderDetails.setAmount(BigDecimal.valueOf(200000));

        customers.setName("Fahmy");
        customers.setEmail("fahmy@tiket.com");
        customers.setPhone("08123123123");
        customers.setAddress("Jl. Jalan");

        Map<Object, String> additionalInfo = new HashMap<>();
        additionalInfo.put("payLater", "true");

        ordersRequest.setInvoiceNumber("INV-123");
        ordersRequest.setAmount(BigDecimal.valueOf(5000000));
        ordersRequest.setCustomer(customers);
        ordersRequest.setOrderDetails(List.of(orderDetails));
        ordersRequest.setAdditionalInfo(additionalInfo);

        ordersResponse.setId(UUID.randomUUID().toString());
        ordersResponse.setInvoiceNumber("INV-123");
        ordersResponse.setAmount(BigDecimal.valueOf(5000000));
        ordersResponse.setCustomer(customers);
        ordersResponse.setOrderDetails(List.of(orderDetails));
        ordersResponse.setAdditionalInfo(additionalInfo);

        orders.setId("7999342d-708d-4937-bd63-87b9316eb223");
        orders.setInvoiceNumber("INV-123");
        orders.setAmount(BigDecimal.valueOf(5000000));
        orders.setCustomer(customers);
        orders.setOrderDetails(List.of(orderDetails));
        orders.setAdditionalInfo(additionalInfo);
    }

    // Arrange, Act & Assert
    @Test
    void createOrder() {
        Mockito.when(ordersRepository.findByInvoiceNumberEqualsIgnoreCase(Mockito.anyString())).thenReturn(Mono.empty());
        Mockito.when(ordersRepository.save(Mockito.any())).thenReturn(Mono.just(orders));

        Mono<Object> responseMono = ordersService.createOrder(ordersRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    assertNotNull(response);
                    assertInstanceOf(OrdersResponse.class, response); // Check instance type
                    OrdersResponse res = (OrdersResponse) response;
                    assertEquals(ordersRequest.getInvoiceNumber(), res.getInvoiceNumber());
                    assertEquals(ordersRequest.getAmount(), res.getAmount());
                    return true;
                })
                .verifyComplete();
    }


    @Test
    void findAllOrders() {
        Mockito.when(ordersRepository.findAll()).thenReturn(Flux.just(orders));
        Mockito.when(ordersRepository.count()).thenReturn(Mono.just(1L));

        Mono<Page<OrdersResponse>> pageResponse = ordersService.findAllOrders(PageRequest.of(0,1));

        StepVerifier.create(pageResponse)
                .expectNextMatches(response -> {
                    assertNotNull(response);
                    assertEquals(response.getTotalElements(), 1);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void findOrderById() {
        Mockito.when(ordersRepository.findById(Mockito.anyString())).thenReturn(Mono.just(orders));

        Mono<OrdersResponse> responseMono = ordersService.findOrderById(orders.getId());

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    assertNotNull(response);
                    assertEquals(ordersRequest.getInvoiceNumber(), response.getInvoiceNumber());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void updateOrder() {
        ordersRequest.setAmount(BigDecimal.valueOf(999999));
        Mockito.when(ordersRepository.findById(Mockito.anyString())).thenReturn(Mono.just(orders));
        Mockito.when(ordersRepository.save(Mockito.any())).thenReturn(Mono.just(orders));

        Mono<OrdersResponse> responseMono = ordersService.updateOrder(ordersRequest, orders.getId());

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> {
                    assertNotNull(response);
                    assertEquals(ordersRequest.getAmount(), response.getAmount());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void deleteOrder() {
        Mockito.when(ordersRepository.findById(Mockito.anyString())).thenReturn(Mono.just(orders));
        Mockito.when(ordersRepository.delete(orders)).thenReturn(Mono.empty());

        Mono<Void> responseMono = ordersService.deleteOrder(orders.getId());

        StepVerifier.create(responseMono)
                .verifyComplete();
        Mockito.verify(ordersRepository, Mockito.times(1)).findById(orders.getId());
        Mockito.verify(ordersRepository, Mockito.times(1)).delete(orders);
    }
}