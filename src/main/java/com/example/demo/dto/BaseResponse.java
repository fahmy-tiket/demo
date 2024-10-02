package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;

@Data
@Builder
public class BaseResponse {
    private int responseCode;
    private String responseMessage;
    private Object data = Collections.emptyList();
}
