package com.example.demo.exception.handler;

import com.example.demo.controller.BaseController;
import com.example.demo.dto.BaseResponse;
import com.example.demo.exception.OrdersAlreadyExistException;
import com.example.demo.exception.OrdersNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestControllerAdvice
public class ApiException extends BaseController {

    @ExceptionHandler(value = OrdersNotFoundException.class)
    public Mono<ResponseEntity<BaseResponse>> handleOrdersNotFoundException(OrdersNotFoundException e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(BaseResponse.builder()
                        .responseCode(HttpStatus.NOT_FOUND.value())
                        .responseMessage(e.getMessage())
                        .data(Collections.emptyList())
                        .build()));
    }

    @ExceptionHandler(value = OrdersAlreadyExistException.class)
    public Mono<ResponseEntity<BaseResponse>> handleOrdersAlreadyExistException(OrdersAlreadyExistException e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(BaseResponse.builder()
                        .responseCode(HttpStatus.CONFLICT.value())
                        .responseMessage(e.getMessage())
                        .data(Collections.emptyList())
                        .build()));
    }
}
