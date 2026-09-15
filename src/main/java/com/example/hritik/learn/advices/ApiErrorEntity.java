package com.example.hritik.learn.advices;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data // to generate getters and setters
@Builder
public class ApiErrorEntity { // This entity class is what is returned when an API fails
    private String status;
    private String message;
    private List<String> subErrors;
}
