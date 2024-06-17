package com.example.reservation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> serviceExceptionHandler(final ServiceException e) {
        log.warn("Service Exception: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(FormException.class)
    public ResponseEntity<String> formExceptionHandler(final FormException e) {
        log.warn("Form Exception: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
