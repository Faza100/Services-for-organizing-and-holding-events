package com.example.my_progect.exeptionHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageError> handlerValidationExeption(
            MethodArgumentNotValidException e) {
        var message = new MessageError(
                "Validation error",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(
                HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageError> handleIllegalArgumentException(
            IllegalArgumentException e) {
        var message = new MessageError(
                "Not Found",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(
                HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageError> handleGlobalException(
            Exception e) {
        var message = new MessageError(
                "Exception",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

}
