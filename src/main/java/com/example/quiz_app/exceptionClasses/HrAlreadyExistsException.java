package com.example.quiz_app.exceptionClasses;

public class HrAlreadyExistsException extends RuntimeException {
    public HrAlreadyExistsException(String message) {
        super(message);
    }
}
