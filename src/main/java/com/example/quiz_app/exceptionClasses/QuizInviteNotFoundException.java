package com.example.quiz_app.exceptionClasses;

public class QuizInviteNotFoundException extends RuntimeException {
    public QuizInviteNotFoundException(String message) {
        super(message);
    }
}
