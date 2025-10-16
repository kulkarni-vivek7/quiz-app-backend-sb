package com.example.quiz_app.exceptionClasses;

public class InviteTokenAlreadyUsedException extends RuntimeException {
    public InviteTokenAlreadyUsedException(String message) {
        super(message);
    }
}
