package com.example.quiz_app.exceptionClasses;

public class OtpAlreadyUsedException extends RuntimeException {
    public OtpAlreadyUsedException(String message) {
        super(message);
    }
}
