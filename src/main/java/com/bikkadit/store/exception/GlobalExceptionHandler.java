package com.bikkadit.store.exception;

import com.bikkadit.store.dto.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * @param ex
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
     public ResponseEntity<ApiResponseMessage>ResourceNotFoundExceptionHandler(ResourceNotFoundException ex)
     {
         LOGGER.info("Exception Handler invoked !!");
         String message = ex.getMessage();
         ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
         return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
     }
}