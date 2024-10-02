package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasePaginatedResponse {
    private int responseCode;
    private String responseMessage;
    private Object data;
    private Pagination pagination;
}
