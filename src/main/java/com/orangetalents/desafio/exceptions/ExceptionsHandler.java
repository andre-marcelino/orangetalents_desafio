package com.orangetalents.desafio.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<ExceptionResponse> handleSecurity(ServiceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> handleSecurity(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(ex.getMessage()));
    }
    
    @Override
    	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    			HttpHeaders headers, HttpStatus status, WebRequest request) {
    	String msg = ex.getAllErrors().stream().findFirst().get().getDefaultMessage();
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(msg));
    	}
}
