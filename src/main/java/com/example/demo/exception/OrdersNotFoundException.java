package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrdersNotFoundException extends RuntimeException {
    public OrdersNotFoundException(String message) {
        super(message);
        log.warn("OrdersNotFoundException: {}", message);
    }
}
