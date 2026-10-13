package com.example.hritik.learn.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

/*
Sample response be like (after wrapping into new schema):
  {
    "data": {
        "id": 1,
        "name": "Ghochu",
        "age": 80,
        "role": "ADMIN",
        "email": "patch@gmail.com",
        "dateOfJoining": "2026-01-02",
        "salary": 250000.12,
        "isActive": true
    },
    "error": null,
    "timestamp": "2026-09-15T21:06:13.401498"
}
*/
@Data // generate getter and setter because fields are private
public class ApiResponseEntity<T> {
    // A) fields
    @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime timestamp;

    // one of the fields below is always non-null and another always null
    private T data;
    private ApiErrorEntity error;



    // B) Constructors
    // Default Constructor
    public ApiResponseEntity() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponseEntity(T data) {
        this(); // calls Default constructor
        this.data = data;
    }

    public ApiResponseEntity(ApiErrorEntity error) {
        this(); // calls Default constructor
        this.error = error;
    }


}
