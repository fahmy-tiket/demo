package com.example.demo.module.orders.dto.response;

import com.example.demo.entity.Customers;
import com.example.demo.entity.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersResponse {
    private String id;
    private String invoiceNumber;
    private BigDecimal amount;
    private Customers customer;
    private List<OrderDetails> orderDetails;
    private Map<Object, String> additionalInfo;
}
