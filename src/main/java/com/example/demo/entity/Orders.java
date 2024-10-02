package com.example.demo.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "orders")
public class Orders {
    @Id
    private String id;
    @Field(value = "invoice_number")
    private String invoiceNumber;
    @Field
    private BigDecimal amount;
    private Customers customer;
    @Field(value = "order_details")
    private List<OrderDetails> orderDetails;
    @Field(value = "additional_info")
    private Map<Object, String> additionalInfo;
}
