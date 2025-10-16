package com.example.quiz_app.exceptionClasses;

public class InvalidInviteTokenException extends RuntimeException {
    public InvalidInviteTokenException(String message) {
        super(message);
    }
}
