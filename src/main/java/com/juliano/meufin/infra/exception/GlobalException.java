package com.juliano.meufin.infra.exception;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    ResponseEntity<HandleException> treatmentIntegrityException(SQLIntegrityConstraintViolationException ex) {
        HandleException exception = new HandleException(ex.getMessage(), 500);
        return  new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictException.class)
    ResponseEntity<HandleException> treatmentConflictException(ConflictException ex) {
        HandleException exception = new HandleException(ex.getMessage(), HttpStatus.CONFLICT.value());
        return  new ResponseEntity<>(exception, HttpStatus.CONFLICT);
    }
}