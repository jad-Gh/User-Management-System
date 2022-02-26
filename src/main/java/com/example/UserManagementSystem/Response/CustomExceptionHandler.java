package com.example.UserManagementSystem.Response;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler{

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<Object> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, WebRequest request) {

        return new ResponseEntity<Object>(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getLocalizedMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .errors(List.of(e.toString()))
                        .build()
                , new HttpHeaders()
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException e, WebRequest request) {

        return new ResponseEntity<Object>(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getLocalizedMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .errors(List.of(e.toString()))
                        .build()
                , new HttpHeaders()
                , HttpStatus.BAD_REQUEST);
    }

    //Handle type mismatch issues
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException e, WebRequest request) {
        String error =
                e.getName() + " should be of type " + e.getRequiredType().getName();
        return new ResponseEntity<Object>(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getLocalizedMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .errors(List.of(error))
                        .build()
                , new HttpHeaders()
                , HttpStatus.BAD_REQUEST);
    }

    //General exception handler mainly for internal server errors
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception e, WebRequest request) {
        return new ResponseEntity<Object>(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .message(e.getLocalizedMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errors(List.of("An Error Occurred"))
                        .build(),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
