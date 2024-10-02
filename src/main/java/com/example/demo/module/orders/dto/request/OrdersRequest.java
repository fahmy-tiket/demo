package com.example.demo.module.orders.dto.request;

import com.example.demo.entity.Customers;
import com.example.demo.entity.OrderDetails;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class OrdersRequest {
    private String invoiceNumber;
    private BigDecimal amount;
    private Customers customer;
    private List<OrderDetails> orderDetails;
    private Map<Object, String> additionalInfo;
}
