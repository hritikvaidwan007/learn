package com.example.hritik.learn.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice // this annotation makes below given exception handler class as global.  for all the controller classes to use it
public class GlobalExceptionHandler {

      // Bad way: it will return 200
//    @ExceptionHandler(NoSuchElementException.class)
//    public String handleEmployeeNotFound(NoSuchElementException exception) {
//        return "Employee was not found";
//    }

    // Good way: it will return actual status code because we are using response entity
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<String> handleResourceNotFound(NoSuchElementException exception) {
//        // Method 1: using builder
//        //return ResponseEntity.notFound().build();
//
//        // Method 2: using constructor (check)
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND); // you can manually pass string instead of exception.getMessage()
//    }

    // Good way 2: using our own AuditErrorEntity class as response entity
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorEntity> handleResourceNotFound(NoSuchElementException exception) {
        ApiErrorEntity apiErrorEntity = ApiErrorEntity.builder() // new keyword is not needed when using lombok builder
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .message(exception.getMessage())
                .build();

        // Method 2: using constructor (check)
        return new ResponseEntity<>(apiErrorEntity, HttpStatus.NOT_FOUND); // you can manually pass string instead of exception.getMessage()
    }
}
