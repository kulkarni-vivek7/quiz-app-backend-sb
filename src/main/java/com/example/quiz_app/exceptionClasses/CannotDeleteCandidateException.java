package com.example.quiz_app.exceptionClasses;

public class CannotDeleteCandidateException extends RuntimeException {
    public CannotDeleteCandidateException(String message) {
        super(message);
    }
}
