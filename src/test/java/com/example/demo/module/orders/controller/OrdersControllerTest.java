package com.example.demo.module.orders.controller;

import com.example.demo.dto.BaseResponse;
import com.example.demo.entity.Customers;
import com.example.demo.entity.OrderDetails;
import com.example.demo.entity.Orders;
import com.example.demo.module.orders.dto.request.OrdersRequest;
import com.example.demo.module.orders.dto.response.OrdersResponse;
import com.example.demo.module.orders.service.OrdersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(OrdersController.class)
class OrdersControllerTest {

    @MockBean
    private OrdersService ordersService;

    private WebTestClient webTestClient;
    private ObjectMapper objectMapper = new ObjectMapper();
    public static OrdersRequest ordersRequest = new OrdersRequest();
    public static OrdersResponse ordersResponse = new OrdersResponse();
    public static Orders orders = new Orders();
    public static OrderDetails orderDetails = new OrderDetails();
    public static Customers customers = new Customers();
    public static BaseResponse baseResponse;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(new OrdersController(ordersService)).build();
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

        ordersResponse.setId("7999342d-708d-4937-bd63-87b9316eb223");
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

        baseResponse = BaseResponse.builder()
                .responseCode(HttpStatus.CREATED.value())
                .responseMessage(HttpStatus.CREATED.name())
                .data(ordersResponse)
                .build();
    }

    @Test
    void createOrder() {
        Mockito.when(ordersService.createOrder(Mockito.any(OrdersRequest.class)))
                .thenReturn(Mono.just(baseResponse));

        webTestClient.post()
                .uri("/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BaseResponse.class)
                .value(response -> {
                    assertEquals(HttpStatus.CREATED.name(), response.getResponseMessage());
                    assertEquals(HttpStatus.CREATED.value(), response.getResponseCode());
                });
    }

    @Test
    void getAllOrders() {
        Page<OrdersResponse> ordersPage = new PageImpl<>(List.of(ordersResponse));

        Mockito.when(ordersService.findAllOrders(Mockito.any(Pageable.class)))
                .thenReturn(Mono.just(ordersPage));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/orders")
                        .queryParam("page", 1)
                        .queryParam("limit", 10)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrdersResponse.class)
                .value(responses -> {
                    assertEquals(1, responses.size());
                    assertEquals("INV-123", responses.get(0).getInvoiceNumber());
                });
    }

    @Test
    void getOrdersById() {
        Mockito.when(ordersService.findOrderById(Mockito.anyString()))
                .thenReturn(Mono.just(ordersResponse));

        webTestClient.get()
                .uri("/v1/orders/{ordersId}", "7999342d-708d-4937-bd63-87b9316eb223")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BaseResponse.class)
                .value(response -> {
                    OrdersResponse ordersResponse1 = objectMapper.convertValue(response.getData(), OrdersResponse.class);
                    assertEquals("7999342d-708d-4937-bd63-87b9316eb223", ordersResponse1.getId());
                    assertEquals("INV-123", ordersResponse1.getInvoiceNumber());
                    assertEquals(HttpStatus.OK.value(), response.getResponseCode());
                    assertEquals(HttpStatus.OK.name(), response.getResponseMessage());
                });
    }

    @Test
    void updateOrder() {
        ordersRequest.setAmount(BigDecimal.valueOf(999999));
        ordersResponse.setAmount(ordersRequest.getAmount());
        Mockito.when(ordersService.updateOrder(Mockito.any(OrdersRequest.class), Mockito.anyString()))
                .thenReturn(Mono.just(ordersResponse));

        webTestClient.patch()
                .uri("/v1/orders/{ordersId}", "7999342d-708d-4937-bd63-87b9316eb223")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ordersRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BaseResponse.class)
                .value(response -> {
                    OrdersResponse ordersResponse1 = objectMapper.convertValue(response.getData(), OrdersResponse.class);
                    assertEquals("7999342d-708d-4937-bd63-87b9316eb223", ordersResponse1.getId());
                    assertEquals(BigDecimal.valueOf(999999), ordersResponse1.getAmount());
                    assertEquals(HttpStatus.OK.value(), response.getResponseCode());
                    assertEquals(HttpStatus.OK.name(), response.getResponseMessage());
                });
    }

    @Test
    void deleteOrder() {
        Mockito.when(ordersService.deleteOrder(Mockito.anyString()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/v1/orders/{ordersId}", "7999342d-708d-4937-bd63-87b9316eb223")
                .exchange()
                .expectStatus().isNoContent();
    }
}