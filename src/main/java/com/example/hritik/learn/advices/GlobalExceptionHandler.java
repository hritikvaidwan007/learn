package com.example.hritik.learn.advices;

import exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// Remember you can throw exception from Controller or Service. anywhere is possible!

@RestControllerAdvice // this annotation makes below given exception handler class as global.  for all the controller classes to use it
public class GlobalExceptionHandler {

    // HANDLER 1:
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

//    // Good way 2: using our own AuditErrorEntity class as response entity
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<ApiErrorEntity> handleResourceNotFound(NoSuchElementException exception) {
//        ApiErrorEntity apiErrorEntity = ApiErrorEntity.builder() // new keyword is not needed when using lombok builder
//                .status(String.valueOf(HttpStatus.NOT_FOUND))
//                .message(exception.getMessage())
//                .build();
//
//        // Method 2: using constructor (check)
//        return new ResponseEntity<>(apiErrorEntity, HttpStatus.NOT_FOUND); // you can manually pass string instead of exception.getMessage()
//    }

//    // Good way 3: using our own ResourceNotFoundException exception class
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiErrorEntity> handleResourceNotFound(ResourceNotFoundException exception) {
//        ApiErrorEntity apiErrorEntity = ApiErrorEntity.builder() // new keyword is not needed when using lombok builder
//                .status(String.valueOf(HttpStatus.NOT_FOUND))
//                .message(exception.getMessage())
//                .build();
//
//        // Method 2: using constructor (check)
//        return new ResponseEntity<>(apiErrorEntity, HttpStatus.NOT_FOUND); // you can manually pass string instead of exception.getMessage()
//    }

    // Good way 4: using ApiResponseEntity (wraps normal response and error into a copmmon schema for UI)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseEntity<?>> handleResourceNotFound(ResourceNotFoundException exception) {
        ApiErrorEntity apiErrorEntity = ApiErrorEntity.builder() // new keyword is not needed when using lombok builder
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .message(exception.getMessage())
                .build();

        return buildErrorResponseEntity(apiErrorEntity); // this function is just defined below
    }

    // HANDLER 2:
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiErrorEntity> handleInputValidationErrors(MethodArgumentNotValidException exception) {
//
//        // first collect all the errors that are raised because of multiple invalid arguments.
//        // 'MethodArgumentNotValidException' exception can be huge bacause of multiple invalid arugements
//        List<String> errors = exception
//                .getBindingResult()
//                .getAllErrors()
//                .stream()
//                .map(error -> error.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        ApiErrorEntity apiErrorEntity = ApiErrorEntity.builder() // new keyword is not needed when using lombok builder
//                .status(String.valueOf(HttpStatus.BAD_REQUEST))
//                .message("Input params validation failed") // toString() method to convert 'List<String>' to 'String'
//                .subErrors(errors) // subErrors we created inside ApiErrorEntity class
//                .build();
//
//        // Method 2: using constructor (check)
//        return new ResponseEntity<>(apiErrorEntity, HttpStatus.BAD_REQUEST);
//    }

    // HANDLER 2: Using UI generic 'ApiResponseEntity' instead of 'ApiErrorEntity' and also using DRY buildErrorResponseEntity() wrapper function
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseEntity<?>> handleInputValidationErrors(MethodArgumentNotValidException exception) {

        // first collect all the errors that are raised because of multiple invalid arguments.
        // 'MethodArgumentNotValidException' exception can be huge bacause of multiple invalid arugements
        List<String> errors = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErrorEntity apiErrorEntity = ApiErrorEntity.builder() // new keyword is not needed when using lombok builder
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message("Input params validation failed") // toString() method to convert 'List<String>' to 'String'
                .subErrors(errors) // subErrors we created inside ApiErrorEntity class
                .build();

        // Method 2: using constructor (check)
        return buildErrorResponseEntity(apiErrorEntity);
    }



    // HANDLER 3:
    // Exception.class is parent of all exception, so it will be called when no OTHER defined exception is found
    // Even validation exception wll be handeled by this if those are not handled somewere else above
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorEntity> handleOtherException(Exception exception) {
//
//        ApiErrorEntity apiErrorEntity = ApiErrorEntity.builder() // new keyword is not needed when using lombok builder
//                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
//                .message(exception.getMessage()) // exception.getMessage() is huge springboot error log because 'exception' is not manually passed by any function (check)
//                .build();
//
//        return new ResponseEntity<>(apiErrorEntity, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    // HANDLER 3:Using UI generic 'ApiResponseEntity' instead of 'ApiErrorEntity' and also using DRY buildErrorResponseEntity() wrapper function
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseEntity<?>> handleOtherException(Exception exception) {
        // Step 1: build api error entity
        ApiErrorEntity apiErrorEntity = ApiErrorEntity.builder() // new keyword is not needed when using lombok builder
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .message(exception.getMessage()) // exception.getMessage() is huge springboot error log because 'exception' is not manually passed by any function (check)
                .build();

        // Step 2: Wrap api error enity inside UI generic 'ApiResponseEntity' using 'buildErrorResponseEntity' function
        return buildErrorResponseEntity(apiErrorEntity);
    }





    // ***************** Internal methods to facilitate 'DRY' *********************************************************
    // remember 'ResponseEntity' helps to return status code
    // we are wrapping apiErrorEntity schema inside ApiResponseEntity schema
    // this is AI generated, this needs to be discussed and fixed
    private ResponseEntity<ApiResponseEntity<?>> buildErrorResponseEntity(ApiErrorEntity apiErrorEntity) {
        // Step 1: Read status string from ApiErrorEntity (set via String.valueOf(HttpStatus.BAD_REQUEST) etc.)
        // That gives values like "400 BAD_REQUEST", but HttpStatus.valueOf() only accepts the enum name "BAD_REQUEST"
        String status = apiErrorEntity.getStatus();
        int spaceIndex = status.indexOf(' ');

        HttpStatus httpStatus;
        if (spaceIndex >= 0) {
            String statusName = status.substring(spaceIndex + 1); // e.g. BAD_REQUEST
            httpStatus = HttpStatus.valueOf(statusName);
        } else {
            httpStatus = HttpStatus.valueOf(status);
        }

        // Step 2: Put ApiErrorEntity inside ApiResponseEntity (error field set, data stays null)
        ApiResponseEntity<?> apiResponseEntity = new ApiResponseEntity<>(apiErrorEntity);

        // Step 3: Return wrapped body with the correct HTTP status code (400, 404, 500, ...)
        return new ResponseEntity<>(apiResponseEntity, httpStatus);
    }


}
