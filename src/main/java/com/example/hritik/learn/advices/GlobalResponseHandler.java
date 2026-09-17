package com.example.hritik.learn.advices;

import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

// 'supports()' and 'beforeBodyWrite()' function and ovveride already added by intellij
// with by default value 'false' and 'null'
// Now UI get reponse in the schema it is desiring (wrapping logic)
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> { // Object means any type of object

    // it helps application to know which api responses needs to be converted/wrapped into some other schema
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // direct 'true' supports conversion of each and every api response. not checking 'returnType'
    }

    // beforeBodyWrite receives 'body' which is planned to be returned to the client, but
    // we will be wrapping 'body' and then return, so UI can always see the desired schema
    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //return null;

        // if already in 'ApiResponseEntity' schema
        if (body instanceof ApiResponseEntity<?>) {
            return body;
        }

        // else
        return new ApiResponseEntity<>(body); // wrapping original response body into ApiResponseEntity
    }
}
