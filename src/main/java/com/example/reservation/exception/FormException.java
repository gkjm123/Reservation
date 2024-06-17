package com.example.reservation.exception;

import lombok.Getter;

@Getter
public class FormException extends RuntimeException {
    private final String message;

    public FormException(String message) {
        super(message);
        this.message = message;
    }
}
