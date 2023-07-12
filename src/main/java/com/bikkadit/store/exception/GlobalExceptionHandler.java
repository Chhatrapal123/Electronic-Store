package com.bikkadit.store.exception;

import com.bikkadit.store.dto.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

     // MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<Map<String,Object>>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
     {
         List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
         Map<String,Object>response = new HashMap<>();
         allErrors.stream().forEach(objectError -> {
             String message = objectError.getDefaultMessage();
             String field = ((FieldError) objectError).getField();
             response.put(field,message);
         });
         return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
     }

     //Handle Api Exception
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage>handleApiRequest(BadApiRequest ex)
    {
        LOGGER.info("Bad Api Request !!");
        String message = ex.getMessage();
        ApiResponseMessage response = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}