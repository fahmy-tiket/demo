package com.example.demo.controller;

import com.example.demo.dto.BasePaginatedResponse;
import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.Pagination;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class BaseController {

    public ResponseEntity<BaseResponse> createResponse(HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(BaseResponse.builder()
                        .responseCode(status.value())
                        .responseMessage(status.name())
                        .data(Collections.emptyList())
                        .build());
    }

    public ResponseEntity<BaseResponse> createResponse(HttpStatus status, Object data) {
        return ResponseEntity
                .status(status)
                .body(BaseResponse.builder()
                        .responseCode(status.value())
                        .responseMessage(status.name())
                        .data(data)
                        .build());
    }

    public ResponseEntity<BaseResponse> responseSuccess() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage(HttpStatus.OK.name())
                        .data(Collections.emptyList())
                        .build());
    }

    public ResponseEntity<BaseResponse> responseSuccess(Object data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.builder()
                        .responseCode(HttpStatus.OK.value())
                        .responseMessage(HttpStatus.OK.name())
                        .data(data)
                        .build());
    }

    public ResponseEntity<BaseResponse> responseCreated() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.builder()
                        .responseCode(HttpStatus.CREATED.value())
                        .responseMessage(HttpStatus.CREATED.name())
                        .data(Collections.emptyList())
                        .build());
    }

    public ResponseEntity<BaseResponse> responseCreated(Object data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.builder()
                        .responseCode(HttpStatus.CREATED.value())
                        .responseMessage(HttpStatus.CREATED.name())
                        .data(data)
                        .build());
    }

    public ResponseEntity<BaseResponse> responseDeleted() {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(BaseResponse.builder()
                        .responseCode(HttpStatus.NO_CONTENT.value())
                        .responseMessage(HttpStatus.NO_CONTENT.name())
                        .data(Collections.emptyList())
                        .build());
    }

    public Mono<BasePaginatedResponse> responsePaginatedSuccess(Object data, Pagination pagination) {
        return Mono.just(BasePaginatedResponse.builder()
                .responseCode(HttpStatus.OK.value())
                .responseMessage(HttpStatus.OK.name())
                .data(data)
                .pagination(pagination)
                .build());
    }

}
