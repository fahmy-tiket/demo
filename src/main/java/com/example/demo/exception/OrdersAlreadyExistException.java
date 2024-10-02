package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrdersAlreadyExistException extends RuntimeException {
    public OrdersAlreadyExistException(String message) {
        super(message);
        log.warn("OrderAlreadyExistException: {}", message);
    }
}
