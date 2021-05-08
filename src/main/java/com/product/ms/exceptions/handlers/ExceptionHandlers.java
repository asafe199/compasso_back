package com.product.ms.exceptions.handlers;

import com.product.ms.exceptions.NotFoundException;
import com.product.ms.exceptions.details.ExceptionDetails;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Component
@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(
                ExceptionDetails.builder()
                        .status_code(HttpStatus.BAD_REQUEST.value())
                        .message(fieldsMessage)
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> methodArgument(MethodArgumentTypeMismatchException ex){
        return ResponseEntity.badRequest().body(
                ExceptionDetails.builder()
                        .status_code(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .build());
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> notFoundException(){
        return ResponseEntity.notFound().build();
    }
}
