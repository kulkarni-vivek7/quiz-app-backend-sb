package com.example.quiz_app.exceptionClasses;

public class CandidateNotFoundException extends RuntimeException {

    private final String msg;

    public CandidateNotFoundException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
