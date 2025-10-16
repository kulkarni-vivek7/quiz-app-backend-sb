package com.example.quiz_app.exceptionClasses;

public class InactiveUserAccountException extends RuntimeException {
    public InactiveUserAccountException(String message) {
        super(message);
    }
}
