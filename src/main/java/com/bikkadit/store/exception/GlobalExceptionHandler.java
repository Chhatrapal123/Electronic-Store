package com.bikkadit.store.exception;

import com.bikkadit.store.dto.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler
{
     public ResponseEntity<ApiResponseMessage>ResourceNotFoundExceptionHandler(ResourceNotFoundException ex)
     {
         String message = ex.getMessage();
         ApiResponseMessage apiResponseMessage = new ApiResponseMessage();
         return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.NOT_FOUND);
     }
}